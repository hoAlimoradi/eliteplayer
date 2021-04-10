package com.alimoradi.data.api.deezer

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerService {

    @GET("search/artist&limit=1")
    suspend fun getArtist(
        @Query("q") artistName: String
    ): Response<DeezerArtistResponse>

    @GET("search/track&limit=1")
    suspend fun getTrack(
        @Query("q") trackName: String
    ): Response<DeezerTrackResponse>

    @GET("search/album&limit=1")
    suspend fun getAlbum(
        @Query("q") trackName: String
    ): Response<DeezerAlbumResponse>

}