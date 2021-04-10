package com.alimoradi.core.interactor.playlist

import com.alimoradi.core.MediaId
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import javax.inject.Inject

class RemoveDuplicatesUseCase @Inject constructor(
    private val playlistGateway: PlaylistGateway,
    private val podcastPlaylistGateway: PodcastPlaylistGateway

) {

    suspend operator fun invoke(mediaId: MediaId) {
        val playlistId = mediaId.resolveId

        if (mediaId.isPodcastPlaylist) {
            return podcastPlaylistGateway.removeDuplicated(playlistId)
        }
        return playlistGateway.removeDuplicated(playlistId)
    }
}