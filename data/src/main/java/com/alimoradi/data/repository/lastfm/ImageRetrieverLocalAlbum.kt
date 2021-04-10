package com.alimoradi.data.repository.lastfm

import android.util.Log
import com.alimoradi.core.entity.LastFmAlbum
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.data.db.dao.LastFmDao
import com.alimoradi.data.mapper.toDomain
import com.alimoradi.data.mapper.toModel
import com.alimoradi.data.utils.assertBackgroundThread
import javax.inject.Inject

internal class ImageRetrieverLocalAlbum @Inject constructor(
    private val lastFmDao: LastFmDao
) {

    companion object {
        @JvmStatic
        private val TAG = "D:${ImageRetrieverLocalAlbum::class.java.simpleName}"
    }

    fun mustFetch(albumId: Long): Boolean {
        assertBackgroundThread()
        return lastFmDao.getAlbum(albumId) == null
    }

    fun getCached(id: Id): LastFmAlbum? {
        assertBackgroundThread()
        return lastFmDao.getAlbum(id)?.toDomain()
    }

    fun cache(model: LastFmAlbum) {
        Log.v(TAG, "cache ${model.id}")
        assertBackgroundThread()
        val entity = model.toModel()
        lastFmDao.insertAlbum(entity)
    }

    fun delete(albumId: Long) {
        Log.v(TAG, "delete $albumId")
        lastFmDao.deleteAlbum(albumId)
    }

}