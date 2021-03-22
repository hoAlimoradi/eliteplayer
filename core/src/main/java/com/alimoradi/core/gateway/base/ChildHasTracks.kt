package com.alimoradi.core.gateway.base

import com.alimoradi.core.entity.track.Song
import kotlinx.coroutines.flow.Flow

interface ChildHasTracks<Param> {

    fun getTrackListByParam(param: Param): List<Song>
    fun observeTrackListByParam(param: Param): Flow<List<Song>>

}