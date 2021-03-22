package com.alimoradi.core.gateway

import com.alimoradi.core.entity.favorite.FavoriteEnum
import com.alimoradi.core.entity.favorite.FavoriteStateEntity
import com.alimoradi.core.entity.favorite.FavoriteType
import com.alimoradi.core.entity.track.Song
import kotlinx.coroutines.flow.Flow

interface FavoriteGateway {

    fun getTracks(): List<Song>
    fun getPodcasts(): List<Song>

    fun observeTracks(): Flow<List<Song>>
    fun observePodcasts(): Flow<List<Song>>

    suspend fun addSingle(type: FavoriteType, songId: Long)
    suspend fun addGroup(type: FavoriteType, songListId: List<Long>)

    suspend fun deleteSingle(type: FavoriteType, songId: Long)
    suspend fun deleteGroup(type: FavoriteType, songListId: List<Long>)

    suspend fun deleteAll(type: FavoriteType)

    suspend fun isFavorite(songId: Long): Boolean
    suspend fun isFavoritePodcast(podcastId: Long): Boolean

    fun observeToggleFavorite(): Flow<FavoriteEnum>
    suspend fun updateFavoriteState(state: FavoriteStateEntity)

    suspend fun toggleFavorite()

}