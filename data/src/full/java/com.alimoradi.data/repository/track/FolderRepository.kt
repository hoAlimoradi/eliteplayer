package com.alimoradi.data.repository.track

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.alimoradi.core.MediaId
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.entity.track.Artist
import com.alimoradi.core.entity.track.Folder
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.Path
import com.alimoradi.core.gateway.track.FolderGateway
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.core.prefs.BlacklistPreferences
import com.alimoradi.core.prefs.SortPreferences
import com.alimoradi.core.schedulers.Schedulers
import com.alimoradi.data.db.dao.FolderMostPlayedDao
import com.alimoradi.data.db.entities.FolderMostPlayedEntity
import com.alimoradi.data.mapper.toArtist
import com.alimoradi.data.mapper.toSong
import com.alimoradi.data.queries.FolderQueries
import com.alimoradi.data.repository.BaseRepository
import com.alimoradi.data.repository.ContentUri
import com.alimoradi.data.utils.assertBackground
import com.alimoradi.data.utils.assertBackgroundThread
import com.alimoradi.data.utils.getString
import com.alimoradi.data.utils.queryAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject

internal class FolderRepository @Inject constructor(
    @ApplicationContext context: Context,
    contentResolver: ContentResolver,
    sortPrefs: SortPreferences,
    blacklistPrefs: BlacklistPreferences,
    private val songGateway2: SongGateway,
    private val mostPlayedDao: FolderMostPlayedDao,
    schedulers: Schedulers
) : BaseRepository<Folder, Path>(context, contentResolver, schedulers), FolderGateway {

    private val queries = FolderQueries(contentResolver, blacklistPrefs, sortPrefs)

    init {
        firstQuery()
    }

    override fun registerMainContentUri(): ContentUri {
        return ContentUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true)
    }

    @Suppress("DEPRECATION")
    private fun extractFolders(cursor: Cursor): List<Folder> {
        assertBackgroundThread()
        val pathList = contentResolver.queryAll(cursor) {
            val data = it.getString(MediaStore.Audio.Media.DATA)
            data.substring(0, data.lastIndexOf(File.separator)) // path
        }
        return pathList.asSequence()
            .groupBy { it }
            .entries
            .map { (path, list) ->
                val dirName = path.substring(path.lastIndexOf(File.separator) + 1)
                Folder(
                    dirName.capitalize(),
                    path,
                    list.size
                )
            }.sortedBy { it.title }
    }

    override fun queryAll(): List<Folder> {
        assertBackgroundThread()
        val cursor = queries.getAll(false)
        return extractFolders(cursor)
    }

    override fun getByParam(param: Path): Folder? {
        assertBackgroundThread()
        return channel.valueOrNull?.find { it.path == param }
    }

    override fun getByHashCode(hashCode: Int): Folder? {
        assertBackgroundThread()
        return channel.valueOrNull?.find { it.path.hashCode() == hashCode }
    }

    override fun observeByParam(param: Path): Flow<Folder?> {
        return channel.asFlow()
            .map { list -> list.find { it.path == param } }
            .distinctUntilChanged()
            .assertBackground()
    }

    override fun getTrackListByParam(param: Path): List<Song> {
        assertBackgroundThread()
        val cursor = queries.getSongList(param)
        return contentResolver.queryAll(cursor) { it.toSong() }
    }

    override fun observeTrackListByParam(param: Path): Flow<List<Song>> {
        val contentUri = ContentUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true)
        return observeByParamInternal(contentUri) { getTrackListByParam(param) }
            .assertBackground()
    }

    override fun getAllBlacklistedIncluded(): List<Folder> {
        assertBackgroundThread()
        val cursor = queries.getAll(true)
        return extractFolders(cursor)
    }

    override fun observeMostPlayed(mediaId: MediaId): Flow<List<Song>> {
        val folderPath = mediaId.categoryValue
        return mostPlayedDao.getAll(folderPath, songGateway2)
            .distinctUntilChanged()
            .assertBackground()
    }

    override suspend fun insertMostPlayed(mediaId: MediaId) {
        assertBackgroundThread()
        mostPlayedDao.insertOne(
            FolderMostPlayedEntity(
                0,
                mediaId.leaf!!,
                mediaId.categoryValue
            )
        )
    }

    override fun observeSiblings(param: Path): Flow<List<Folder>> {
        return observeAll()
            .map { it.filter { it.path != param } }
            .distinctUntilChanged()
            .assertBackground()
    }

    override fun observeRelatedArtists(params: Path): Flow<List<Artist>> {
        val contentUri = ContentUri(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, true)
        return observeByParamInternal(contentUri) { extractArtists(queries.getRelatedArtists(params)) }
            .distinctUntilChanged()
            .assertBackground()
    }

    override fun observeRecentlyAdded(path: Path): Flow<List<Song>> {
        val contentUri = ContentUri(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, true)
        return observeByParamInternal(contentUri) {
            val cursor = queries.getRecentlyAdded(path)
            contentResolver.queryAll(cursor) { it.toSong() }
        }
    }

    private fun extractArtists(cursor: Cursor): List<Artist> {
        assertBackgroundThread()
        return contentResolver.queryAll(cursor) { it.toArtist() }
            .groupBy { it.id }
            .map { (_, list) ->
                val artist = list[0]
                artist.withSongs(list.size)
            }
    }
}