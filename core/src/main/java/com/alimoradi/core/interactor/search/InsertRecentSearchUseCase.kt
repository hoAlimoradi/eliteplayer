package com.alimoradi.core.interactor.search

import com.alimoradi.core.MediaId
import com.alimoradi.core.gateway.RecentSearchesGateway
import javax.inject.Inject

class InsertRecentSearchUseCase @Inject constructor(
    private val recentSearchesGateway: RecentSearchesGateway

) {

    suspend operator fun invoke(mediaId: MediaId) {
        val id = mediaId.resolveId
        return when {
            mediaId.isLeaf && !mediaId.isPodcast -> recentSearchesGateway.insertSong(id)
            mediaId.isArtist -> recentSearchesGateway.insertArtist(id)
            mediaId.isAlbum -> recentSearchesGateway.insertAlbum(id)
            mediaId.isPlaylist -> recentSearchesGateway.insertPlaylist(id)
            mediaId.isFolder -> recentSearchesGateway.insertFolder(id)
            mediaId.isGenre -> recentSearchesGateway.insertGenre(id)

            mediaId.isLeaf && mediaId.isPodcast -> recentSearchesGateway.insertPodcast(id)
            mediaId.isPodcastPlaylist -> recentSearchesGateway.insertPodcastPlaylist(id)
            mediaId.isPodcastAlbum -> recentSearchesGateway.insertPodcastAlbum(id)
            mediaId.isPodcastArtist -> recentSearchesGateway.insertPodcastArtist(id)
            else -> throw IllegalArgumentException("invalid category ${mediaId.resolveId}")
        }
    }
}