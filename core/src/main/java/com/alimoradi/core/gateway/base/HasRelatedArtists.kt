package com.alimoradi.core.gateway.base

import com.alimoradi.core.entity.track.Artist
import kotlinx.coroutines.flow.Flow

interface HasRelatedArtists<Param> {
    fun observeRelatedArtists(params: Param): Flow<List<Artist>>
}