package com.alimoradi.data.repository.track

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore.Audio
import android.util.Log
import dev.olog.contentresolversql.querySql
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.core.prefs.BlacklistPreferences
import com.alimoradi.core.prefs.SortPreferences
import com.alimoradi.core.schedulers.Schedulers
import com.alimoradi.data.mapper.toSong
import com.alimoradi.data.queries.TrackQueries
import com.alimoradi.data.repository.BaseRepository
import com.alimoradi.data.repository.ContentUri
import com.alimoradi.data.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.io.File
import javax.inject.Inject

internal class SongRepository @Inject constructor(
    @ApplicationContext context: Context,
    contentResolver: ContentResolver,
    sortPrefs: SortPreferences,
    blacklistPrefs: BlacklistPreferences,
    schedulers: Schedulers
) : BaseRepository<Song, Id>(context, contentResolver, schedulers), SongGateway {

    private val queries = TrackQueries(
        contentResolver, blacklistPrefs,
        sortPrefs, false
    )

    init {
        firstQuery()
    }

    override fun registerMainContentUri(): ContentUri {
        return ContentUri(Audio.Media.EXTERNAL_CONTENT_URI, true)
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
        val uri = ContentUris.withAppendedId(Audio.Media.EXTERNAL_CONTENT_URI, param)
        val contentUri = ContentUri(uri, true)
        return observeByParamInternal(contentUri) { getByParam(param) }
            .distinctUntilChanged()
            .assertBackground()
    }

    override suspend fun deleteSingle(id: Id) {
        return deleteInternal(id)
    }

    override suspend fun deleteGroup(ids: List<Song>) {
        for (id in ids) {
            deleteInternal(id.id)
        }
    }

    private fun deleteInternal(id: Id) {
        assertBackgroundThread()
        val path = getByParam(id)!!.path
        val uri = ContentUris.withAppendedId(Audio.Media.EXTERNAL_CONTENT_URI, id)
        val deleted = contentResolver.delete(uri, null, null)
        if (deleted < 1) {
            Log.w("SongRepo", "song not found $id")
            return
        }

        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
    }

    override fun getByUri(uri: Uri): Song? {
        try {
            val id = getByUriInternal(uri) ?: return null
            return getByParam(id)
        } catch (ex: Exception){
            ex.printStackTrace()
            return null
        }
    }

    private fun getByUriInternal(uri: Uri): Long? {
        // https://developer.android.com/training/secure-file-sharing/retrieve-info
        // content uri has only two field [_id, _display_name]
        val fileQuery = """
            SELECT ${Audio.Media.DISPLAY_NAME}
            FROM $uri
        """
        val displayName = contentResolver.querySql(fileQuery).use {
            it.moveToFirst()
            it.getString(Audio.Media.DISPLAY_NAME)
        }

        val itemQuery = """
            SELECT ${Audio.Media._ID}
            FROM ${Audio.Media.EXTERNAL_CONTENT_URI}
            WHERE ${Audio.Media.DISPLAY_NAME} = ?
        """
        val id = contentResolver.querySql(itemQuery, arrayOf(displayName)).use {
            it.moveToFirst()
            it.getLong(Audio.Media._ID)
        }
        return id
    }

    override fun getByAlbumId(albumId: Id): Song? {
        return channel.valueOrNull?.find { it.albumId == albumId }
    }
}