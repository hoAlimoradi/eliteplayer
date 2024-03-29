package com.alimoradi.data.repository.lastfm

import android.util.Log
import com.alimoradi.core.entity.LastFmTrack
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.data.db.dao.LastFmDao
import com.alimoradi.data.mapper.toDomain
import com.alimoradi.data.mapper.toModel
import com.alimoradi.data.utils.assertBackgroundThread
import javax.inject.Inject

internal class ImageRetrieverLocalTrack @Inject constructor(
    private val lastFmDao: LastFmDao
) {

    companion object {
        @JvmStatic
        private val TAG = "D:${ImageRetrieverLocalTrack::class.java.simpleName}"
    }

    fun mustFetch(trackId: Id): Boolean {
        assertBackgroundThread()
        return lastFmDao.getTrack(trackId) == null
    }

    fun getCached(id: Id): LastFmTrack? {
        return lastFmDao.getTrack(id)?.toDomain()
    }

    fun cache(model: LastFmTrack) {
        Log.v(TAG, "cache ${model.id}")
        assertBackgroundThread()
        val entity = model.toModel()
        lastFmDao.insertTrack(entity)
    }

    fun delete(trackId: Long) {
        Log.v(TAG, "delete $trackId")
        assertBackgroundThread()
        lastFmDao.deleteTrack(trackId)
    }

}