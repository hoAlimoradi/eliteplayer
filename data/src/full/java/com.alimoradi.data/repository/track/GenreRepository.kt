package com.alimoradi.data.repository.track

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.alimoradi.core.MediaId
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.entity.track.Artist
import com.alimoradi.core.entity.track.Genre
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.track.GenreGateway
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.core.prefs.BlacklistPreferences
import com.alimoradi.core.prefs.SortPreferences
import com.alimoradi.core.schedulers.Schedulers
import com.alimoradi.data.db.dao.GenreMostPlayedDao
import com.alimoradi.data.db.entities.GenreMostPlayedEntity
import com.alimoradi.data.mapper.toArtist
import com.alimoradi.data.mapper.toGenre
import com.alimoradi.data.mapper.toPlaylistSong
import com.alimoradi.data.queries.GenreQueries
import com.alimoradi.data.repository.BaseRepository
import com.alimoradi.data.repository.ContentUri
import com.alimoradi.data.utils.assertBackground
import com.alimoradi.data.utils.assertBackgroundThread
import com.alimoradi.data.utils.queryAll
import com.alimoradi.data.utils.queryCountRow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GenreRepository @Inject constructor(
    @ApplicationContext context: Context,
    contentResolver: ContentResolver,
    sortPrefs: SortPreferences,
    blacklistPrefs: BlacklistPreferences,
    private val songGateway2: SongGateway,
    private val mostPlayedDao: GenreMostPlayedDao,
    schedulers: Schedulers
) : BaseRepository<Genre, Id>(context, contentResolver, schedulers), GenreGateway {

    private val queries = GenreQueries(contentResolver, blacklistPrefs, sortPrefs)

    init {
        firstQuery()
    }

    override fun registerMainContentUri(): ContentUri {
        return ContentUri(MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI, true)
    }

    override fun queryAll(): List<Genre> {
        assertBackgroundThread()
        val cursor = queries.getAll()
        val genres = contentResolver.queryAll(cursor) { it.toGenre() }
        return genres.mapNotNull { genre ->
            // get the size for every genre
            val sizeQueryCursor = queries.countGenreSize(genre.id)
            val sizeQuery = contentResolver.queryCountRow(sizeQueryCursor)
            if (sizeQuery == 0){
                null
            } else {
                genre.withSongs(sizeQuery)
            }
        }
    }

    override fun getByParam(param: Id): Genre? {
        assertBackgroundThread()
        return channel.valueOrNull?.find { it.id == param }
    }

    override fun observeByParam(param: Id): Flow<Genre?> {
        return channel.asFlow().map { it.find { it.id == param } }
            .distinctUntilChanged()
            .assertBackground()
    }

    override fun getTrackListByParam(param: Id): List<Song> {
        assertBackgroundThread()
        val cursor = queries.getSongList(param)
        return contentResolver.queryAll(cursor) { it.toPlaylistSong() }
    }

    override fun observeTrackListByParam(param: Id): Flow<List<Song>> {
        val uri = MediaStore.Audio.Genres.Members.getContentUri("external", param)
        val contentUri = ContentUri(uri, true)
        return observeByParamInternal(contentUri) { getTrackListByParam(param) }
            .assertBackground()
    }

    override fun observeSiblings(param: Id): Flow<List<Genre>> {
        return observeAll()
            .map { it.filter { it.id != param } }
            .distinctUntilChanged()
            .assertBackground()
    }

    override fun observeMostPlayed(mediaId: MediaId): Flow<List<Song>> {
        return mostPlayedDao.getAll(mediaId.categoryId, songGateway2)
            .distinctUntilChanged()
            .assertBackground()
    }

    override suspend fun insertMostPlayed(mediaId: MediaId) {
        assertBackgroundThread()
        mostPlayedDao.insertOne(
            GenreMostPlayedEntity(
                0,
                mediaId.leaf!!,
                mediaId.categoryId
            )
        )
    }

    override fun observeRelatedArtists(params: Id): Flow<List<Artist>> {
        val contentUri = ContentUri(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, true)
        return observeByParamInternal(contentUri) { extractArtists(queries.getRelatedArtists(params)) }
            .distinctUntilChanged()
            .assertBackground()
    }

    override fun observeRecentlyAdded(path: Id): Flow<List<Song>> {
        val contentUri = ContentUri(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, true)
        return observeByParamInternal(contentUri) {
            val cursor = queries.getRecentlyAdded(path)
            contentResolver.queryAll(cursor) { it.toPlaylistSong() }
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