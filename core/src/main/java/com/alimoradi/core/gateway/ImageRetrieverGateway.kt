package com.alimoradi.core.gateway

import com.alimoradi.core.entity.LastFmAlbum
import com.alimoradi.core.entity.LastFmArtist
import com.alimoradi.core.entity.LastFmTrack
import com.alimoradi.core.gateway.base.Id

interface ImageRetrieverGateway {
    suspend fun mustFetchTrack(trackId: Id): Boolean
    suspend fun getTrack(trackId: Id): LastFmTrack?
    suspend fun deleteTrack(trackId: Id)

    suspend fun mustFetchAlbum(albumId: Id): Boolean
    suspend fun getAlbum(albumId: Id): LastFmAlbum?
    suspend fun deleteAlbum(albumId: Id)

    suspend fun mustFetchArtist(artistId: Id): Boolean
    suspend fun getArtist(artistId: Id): LastFmArtist?
    suspend fun deleteArtist(artistId: Id)
} 