package com.alimoradi.data.repository.podcast

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.entity.track.Album
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.HasLastPlayed
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.podcast.PodcastAlbumGateway
import com.alimoradi.core.prefs.BlacklistPreferences
import com.alimoradi.core.prefs.SortPreferences
import com.alimoradi.core.schedulers.Schedulers
import com.alimoradi.data.db.dao.LastPlayedPodcastAlbumDao
import com.alimoradi.data.mapper.toAlbum
import com.alimoradi.data.mapper.toSong
import com.alimoradi.data.queries.AlbumsQueries
import com.alimoradi.data.repository.BaseRepository
import com.alimoradi.data.repository.ContentUri
import com.alimoradi.data.utils.assertBackground
import com.alimoradi.data.utils.assertBackgroundThread
import com.alimoradi.data.utils.queryAll
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class PodcastAlbumRepository @Inject constructor(
    @ApplicationContext context: Context,
    contentResolver: ContentResolver,
    sortPrefs: SortPreferences,
    blacklistPrefs: BlacklistPreferences,
    private val lastPlayedDao: LastPlayedPodcastAlbumDao,
    schedulers: Schedulers
) : BaseRepository<Album, Id>(context, contentResolver, schedulers), PodcastAlbumGateway {

    private val queries = AlbumsQueries(contentResolver, blacklistPrefs, sortPrefs, true)

    init {
        firstQuery()
    }

    override fun registerMainContentUri(): ContentUri {
        return ContentUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true)
    }

    private fun extractAlbums(cursor: Cursor): List<Album> {
        assertBackgroundThread()
        return contentResolver.queryAll(cursor) { it.toAlbum() }
            .groupBy { it.id }
            .map { (_, list) ->
                val album = list[0]
                album.withSongs(list.size)
            }
    }

    override fun queryAll(): List<Album> {
        assertBackgroundThread()
        val cursor = queries.getAll()
        return extractAlbums(cursor)
    }

    override fun getByParam(param: Id): Album? {
        assertBackgroundThread()
        return channel.valueOrNull?.find { it.id == param }
    }

    override fun observeByParam(param: Id): Flow<Album?> {
        return channel.asFlow().map { list -> list.find { it.id == param } }
            .distinctUntilChanged()
            .assertBackground()
    }

    override fun getTrackListByParam(param: Id): List<Song> {
        assertBackgroundThread()
        val cursor = queries.getSongList(param)
        return contentResolver.queryAll(cursor) { it.toSong() }
    }

    override fun observeTrackListByParam(param: Id): Flow<List<Song>> {
        val contentUri = ContentUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true)
        return observeByParamInternal(contentUri) { getTrackListByParam(param) }
            .assertBackground()
    }

    override fun observeLastPlayed(): Flow<List<Album>> {
        return observeAll().combine(lastPlayedDao.getAll()) { all, lastPlayed ->
            if (all.size < HasLastPlayed.MIN_ITEMS) {
                listOf() // too few album to show recents
            } else {
                lastPlayed.asSequence()
                    .mapNotNull { last -> all.firstOrNull { it.id == last.id } }
                    .take(HasLastPlayed.MAX_ITEM_TO_SHOW)
                    .toList()
            }
        }.distinctUntilChanged()
            .assertBackground()
    }

    override suspend fun addLastPlayed(id: Id) {
        assertBackgroundThread()
        lastPlayedDao.insertOne(id)
    }

    override fun observeRecentlyAdded(): Flow<List<Album>> {
        val contentUri = ContentUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true)
        return observeByParamInternal(contentUri) { extractAlbums(queries.getRecentlyAdded()) }
            .distinctUntilChanged()
            .assertBackground()
    }

    override fun observeSiblings(param: Id): Flow<List<Album>> {
        return observeAll()
            .map { it.filter { it.id != param } }
            .distinctUntilChanged()
            .assertBackground()
    }

    override fun observeArtistsAlbums(artistId: Id): Flow<List<Album>> {
        return observeAll()
            .map { it.filter { it.artistId != artistId } }
            .distinctUntilChanged()
            .assertBackground()
    }
}