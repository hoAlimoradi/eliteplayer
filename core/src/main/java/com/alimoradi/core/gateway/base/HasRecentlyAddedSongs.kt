package com.alimoradi.core.gateway.base

import com.alimoradi.core.entity.track.Song
import kotlinx.coroutines.flow.Flow

interface HasRecentlyAddedSongs <Param> {
    fun observeRecentlyAdded(path: Param): Flow<List<Song>>
}