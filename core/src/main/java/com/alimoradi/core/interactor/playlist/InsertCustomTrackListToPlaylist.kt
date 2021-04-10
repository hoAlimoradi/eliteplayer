package com.alimoradi.core.interactor.playlist

import com.alimoradi.core.entity.PlaylistType
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import javax.inject.Inject

class InsertCustomTrackListToPlaylist @Inject constructor(
    private val playlistGateway: PlaylistGateway,
    private val podcastPlaylistGateway: PodcastPlaylistGateway

) {

    suspend operator fun invoke(param: InsertCustomTrackListRequest) {

        if (param.type == PlaylistType.PODCAST) {
            val playlistId = podcastPlaylistGateway.createPlaylist(param.playlistTitle)

            podcastPlaylistGateway.addSongsToPlaylist(playlistId, param.tracksId)
        } else {
            val playlistId = playlistGateway.createPlaylist(param.playlistTitle)

            playlistGateway.addSongsToPlaylist(playlistId, param.tracksId)
        }
    }
}

data class InsertCustomTrackListRequest(
    val playlistTitle: String,
    val tracksId: List<Long>,
    val type: PlaylistType
)