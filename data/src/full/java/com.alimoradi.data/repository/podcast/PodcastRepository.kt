package com.alimoradi.data.repository.podcast

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.prefs.BlacklistPreferences
import com.alimoradi.core.prefs.SortPreferences
import com.alimoradi.core.schedulers.Schedulers
import com.alimoradi.data.db.dao.PodcastPositionDao
import com.alimoradi.data.db.entities.PodcastPositionEntity
import com.alimoradi.data.mapper.toSong
import com.alimoradi.data.queries.TrackQueries
import com.alimoradi.data.repository.BaseRepository
import com.alimoradi.data.repository.ContentUri
import com.alimoradi.data.utils.assertBackground
import com.alimoradi.data.utils.assertBackgroundThread
import com.alimoradi.data.utils.queryAll
import com.alimoradi.data.utils.queryOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.io.File
import javax.inject.Inject

internal class PodcastRepository @Inject constructor(
    @ApplicationContext context: Context,
    contentResolver: ContentResolver,
    sortPrefs: SortPreferences,
    blacklistPrefs: BlacklistPreferences,
    private val podcastPositionDao: PodcastPositionDao,
    schedulers: Schedulers
) : BaseRepository<Song, Id>(context, contentResolver, schedulers), PodcastGateway {

    private val queries = TrackQueries(
        context.contentResolver, blacklistPrefs,
        sortPrefs, true
    )

    init {
        firstQuery()
    }

    override fun registerMainContentUri(): ContentUri {
        return ContentUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true)
    }

    override fun queryAll(): List<Song> {
//        assertBackgroundThread()
        val cursor = queries.getAll()
        return contentResolver.queryAll(cursor) { it.toSong() }
    }

    override fun getByParam(param: Id): Song? {
        assertBackgroundThread()
        val cursor = queries.getByParam(param)
        return contentResolver.queryOne(cursor) { it.toSong() }
    }

    override fun observeByParam(param: Id): Flow<Song?> {
        val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, param)
        val contentUri = ContentUri(uri, true)
        return observeByParamInternal(contentUri) { getByParam(param) }
            .distinctUntilChanged()
            .assertBackground()
    }

    override suspend fun deleteSingle(id: Id) {
        return deleteInternal(id)
    }

    override suspend fun deleteGroup(podcastList: List<Song>) {
        for (id in podcastList) {
            deleteInternal(id.id)
        }
    }

    private fun deleteInternal(id: Id) {
        assertBackgroundThread()
        val path = getByParam(id)!!.path
        val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        val deleted = contentResolver.delete(uri, null, null)

        if (deleted < 1) {
            Log.w("PodcastRepo", "podcast not found $id")
            return
        }
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }

    override fun getCurrentPosition(podcastId: Long, duration: Long): Long {
        assertBackgroundThread()
        val position = podcastPositionDao.getPosition(podcastId) ?: 0L
        if (position > duration - 1000 * 5) {
            // if last 5 sec, restart
            return 0L
        }
        return position
    }

    override fun saveCurrentPosition(podcastId: Long, position: Long) {
        assertBackgroundThread()
        podcastPositionDao.setPosition(PodcastPositionEntity(podcastId, position))
    }

    override fun getByAlbumId(albumId: Id): Song? {
        return channel.valueOrNull?.find { it.albumId == albumId }
    }
}