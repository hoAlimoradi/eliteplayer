package com.alimoradi.data.repository

import com.alimoradi.core.entity.favorite.FavoriteEnum
import com.alimoradi.core.entity.favorite.FavoriteStateEntity
import com.alimoradi.core.entity.favorite.FavoriteType
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.FavoriteGateway
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.data.db.dao.FavoriteDao
import com.alimoradi.data.utils.assertBackground
import com.alimoradi.data.utils.assertBackgroundThread
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao,
    private val songGateway: SongGateway,
    private val podcastGateway: PodcastGateway

) : FavoriteGateway {

    private val favoriteStatePublisher = ConflatedBroadcastChannel<FavoriteStateEntity>()

    override fun observeToggleFavorite(): Flow<FavoriteEnum> = favoriteStatePublisher
        .asFlow()
        .map { it.enum }

    override suspend fun updateFavoriteState(state: FavoriteStateEntity) {
        favoriteStatePublisher.offer(state)
    }

    override fun getTracks(): List<Song> {
        assertBackgroundThread()
        val historyList = favoriteDao.getAllTracksImpl()
        val songList: Map<Long, List<Song>> = songGateway.getAll().groupBy { it.id }
        return historyList.mapNotNull { id -> songList[id]?.get(0) }
    }

    override fun getPodcasts(): List<Song> {
        assertBackgroundThread()
        val historyList = favoriteDao.getAllPodcastsImpl()
        val songList: Map<Long, List<Song>> = songGateway.getAll().groupBy { it.id }
        return historyList.mapNotNull { id -> songList[id]?.get(0) }
    }

    override fun observeTracks(): Flow<List<Song>> {
        return favoriteDao.observeAllTracksImpl()
            .map { favorites ->
                val songs: Map<Long, List<Song>> = songGateway.getAll().groupBy { it.id }
                favorites.mapNotNull { id -> songs[id]?.get(0) }
                    .sortedBy { it.title }
            }.assertBackground()
    }

    override fun observePodcasts(): Flow<List<Song>> {
        return favoriteDao.observeAllPodcastsImpl()
            .map { favorites ->
                val podcast: Map<Long, List<Song>> = podcastGateway.getAll().groupBy { it.id }
                favorites.mapNotNull { id -> podcast[id]?.get(0) }
                    .sortedBy { it.title }
            }.assertBackground()
    }

    override suspend fun addSingle(type: FavoriteType, songId: Long) {
        favoriteDao.addToFavoriteSingle(type, songId)
        val id = favoriteStatePublisher.value.songId
        if (songId == id) {
            updateFavoriteState(
                FavoriteStateEntity(songId, FavoriteEnum.FAVORITE, type)
            )
        }
    }

    override suspend fun addGroup(type: FavoriteType, songListId: List<Long>) {
        favoriteDao.addToFavorite(type, songListId)
        val songId = favoriteStatePublisher.value.songId
        if (songListId.contains(songId)) {
            updateFavoriteState(FavoriteStateEntity(songId, FavoriteEnum.FAVORITE, type))
        }
    }

    override suspend fun deleteSingle(type: FavoriteType, songId: Long) {
        favoriteDao.removeFromFavorite(type, listOf(songId))
        val id = favoriteStatePublisher.value.songId
        if (songId == id) {
            updateFavoriteState(FavoriteStateEntity(songId, FavoriteEnum.NOT_FAVORITE, type))
        }
    }

    override suspend fun deleteGroup(type: FavoriteType, songListId: List<Long>) {
        favoriteDao.removeFromFavorite(type, songListId)
        val songId = favoriteStatePublisher.value.songId
        if (songListId.contains(songId)) {
            updateFavoriteState(
                FavoriteStateEntity(songId, FavoriteEnum.NOT_FAVORITE, type)
            )
        }
    }

    override suspend fun deleteAll(type: FavoriteType) {
        favoriteDao.deleteTracks()
        val songId = favoriteStatePublisher.value.songId
        updateFavoriteState(FavoriteStateEntity(songId, FavoriteEnum.NOT_FAVORITE, type))
    }

    override suspend fun isFavorite(songId: Long): Boolean {
        return favoriteDao.isFavorite(songId)
    }

    override suspend fun isFavoritePodcast(podcastId: Long): Boolean {
        return favoriteDao.isFavoritePodcast(podcastId)
    }

    override suspend fun toggleFavorite() {
        assertBackgroundThread()

        val value = favoriteStatePublisher.valueOrNull ?: return
        val id = value.songId
        val state = value.enum
        val type = value.favoriteType

        when (state) {
            FavoriteEnum.NOT_FAVORITE -> {
                updateFavoriteState(
                    FavoriteStateEntity(id, FavoriteEnum.FAVORITE, type)
                )
                favoriteDao.addToFavoriteSingle(type, id)
            }
            FavoriteEnum.FAVORITE -> {
                updateFavoriteState(
                    FavoriteStateEntity(id, FavoriteEnum.NOT_FAVORITE, type)
                )
                favoriteDao.removeFromFavorite(type, listOf(id))
            }
        }
    }
}