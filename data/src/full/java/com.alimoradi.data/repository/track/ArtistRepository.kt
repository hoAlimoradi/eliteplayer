package com.alimoradi.data.repository.track

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import dev.olog.core.dagger.ApplicationContext
import dev.olog.core.entity.track.Artist
import dev.olog.core.entity.track.Song
import dev.olog.core.gateway.base.HasLastPlayed
import dev.olog.core.gateway.base.Id
import dev.olog.core.gateway.track.ArtistGateway
import dev.olog.core.prefs.BlacklistPreferences
import dev.olog.core.prefs.SortPreferences
import dev.olog.core.schedulers.Schedulers
import com.alimoradi.data.db.dao.LastPlayedArtistDao
import com.alimoradi.data.mapper.toArtist
import com.alimoradi.data.mapper.toSong
import com.alimoradi.data.queries.ArtistQueries
import com.alimoradi.data.repository.BaseRepository
import com.alimoradi.data.repository.ContentUri
import com.alimoradi.data.utils.assertBackground
import com.alimoradi.data.utils.assertBackgroundThread
import com.alimoradi.data.utils.queryAll
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class ArtistRepository @Inject constructor(
    @ApplicationContext context: Context,
    contentResolver: ContentResolver,
    sortPrefs: SortPreferences,
    blacklistPrefs: BlacklistPreferences,
    private val lastPlayedDao: LastPlayedArtistDao,
    schedulers: Schedulers
) : BaseRepository<Artist, Id>(context, contentResolver, schedulers), ArtistGateway {

    private val queries = ArtistQueries(contentResolver, blacklistPrefs, sortPrefs, false)

    init {
        firstQuery()
    }

    override fun registerMainContentUri(): ContentUri {
        return ContentUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true)
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

    override fun queryAll(): List<Artist> {
        assertBackgroundThread()
        val cursor = queries.getAll()
        return extractArtists(cursor)
    }

    override fun getByParam(param: Id): Artist? {
        assertBackgroundThread()
        return channel.valueOrNull?.find { it.id == param }
    }

    override fun observeByParam(param: Id): Flow<Artist?> {
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

    override fun observeLastPlayed(): Flow<List<Artist>> {
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

    override fun observeRecentlyAdded(): Flow<List<Artist>> {
        val contentUri = ContentUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true)
        return observeByParamInternal(contentUri) { extractArtists(queries.getRecentlyAdded()) }
            .distinctUntilChanged()
            .assertBackground()
    }
}