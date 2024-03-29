package com.alimoradi.core.interactor.playlist

import com.alimoradi.core.MediaId
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import javax.inject.Inject

class RenameUseCase @Inject constructor(
    private val playlistGateway: PlaylistGateway,
    private val podcastPlaylistGateway: PodcastPlaylistGateway

) {

    suspend operator fun invoke(mediaId: MediaId, newTitle: String) {
        return when {
            mediaId.isPodcastPlaylist -> podcastPlaylistGateway.renamePlaylist(
                mediaId.categoryValue.toLong(),
                newTitle
            )
            mediaId.isPlaylist -> playlistGateway.renamePlaylist(
                mediaId.categoryValue.toLong(),
                newTitle
            )
            else -> throw IllegalArgumentException("not a folder nor a playlist, $mediaId")
        }
    }
}