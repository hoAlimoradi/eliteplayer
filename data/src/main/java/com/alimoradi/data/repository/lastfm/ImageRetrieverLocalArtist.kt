package com.alimoradi.data.repository.lastfm

import android.util.Log
import dev.olog.core.entity.LastFmArtist
import dev.olog.core.gateway.base.Id
import com.alimoradi.data.db.dao.LastFmDao
import com.alimoradi.data.mapper.toDomain
import com.alimoradi.data.mapper.toModel
import com.alimoradi.data.utils.assertBackgroundThread
import javax.inject.Inject

internal class ImageRetrieverLocalArtist @Inject constructor(
    private val lastFmDao: LastFmDao
) {

    companion object {
        @JvmStatic
        private val TAG = "D:${ImageRetrieverLocalArtist::class.java.simpleName}"
    }

    fun mustFetch(artistId: Long): Boolean {
        assertBackgroundThread()
        return lastFmDao.getArtist(artistId) == null
    }

    fun getCached(id: Id): LastFmArtist? {
        return lastFmDao.getArtist(id)?.toDomain()
    }

    fun cache(model: LastFmArtist) {
        Log.v(TAG, "cache ${model.id}")
        assertBackgroundThread()
        val entity = model.toModel()
        lastFmDao.insertArtist(entity)
    }

    fun delete(artistId: Long) {
        Log.v(TAG, "delete $artistId")
        assertBackgroundThread()
        lastFmDao.deleteArtist(artistId)
    }

}