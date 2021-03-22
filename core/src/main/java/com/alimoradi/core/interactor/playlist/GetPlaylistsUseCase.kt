package com.alimoradi.core.interactor.playlist

import com.alimoradi.core.entity.PlaylistType
import com.alimoradi.core.entity.track.Playlist
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import javax.inject.Inject

class GetPlaylistsUseCase @Inject internal constructor(
    private val playlistGateway: PlaylistGateway,
    private val podcastPlaylistgateway: PodcastPlaylistGateway

) {

    fun execute(type: PlaylistType): List<Playlist> {
        if (type == PlaylistType.PODCAST) {
            return podcastPlaylistgateway.getAll()
        }
        return playlistGateway.getAll()
    }
}
