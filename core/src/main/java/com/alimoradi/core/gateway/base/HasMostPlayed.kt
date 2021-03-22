package com.alimoradi.core.gateway.base

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.track.Song
import kotlinx.coroutines.flow.Flow

interface HasMostPlayed {
    fun observeMostPlayed(mediaId: MediaId): Flow<List<Song>>
    suspend fun insertMostPlayed(mediaId: MediaId)
}