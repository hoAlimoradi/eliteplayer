package com.alimoradi.core.interactor.playlist

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.track.Playlist
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import com.alimoradi.core.interactor.songlist.GetSongListByParamUseCase
import javax.inject.Inject

class AddToPlaylistUseCase @Inject constructor(
    private val playlistGateway: PlaylistGateway,
    private val podcastPlaylistGateway: PodcastPlaylistGateway,
    private val getSongListByParamUseCase: GetSongListByParamUseCase

) {

    suspend operator fun invoke(playlist: Playlist, mediaId: MediaId) {
        if (mediaId.isLeaf && mediaId.isPodcast) {
            podcastPlaylistGateway.addSongsToPlaylist(playlist.id, listOf(mediaId.resolveId))
            return
        }

        if (mediaId.isLeaf) {
            playlistGateway.addSongsToPlaylist(playlist.id, listOf(mediaId.resolveId))
            return
        }

        val songList = getSongListByParamUseCase(mediaId).map { it.id }
        if (mediaId.isAnyPodcast) {
            podcastPlaylistGateway.addSongsToPlaylist(playlist.id, songList)
        } else {
            playlistGateway.addSongsToPlaylist(playlist.id, songList)
        }
    }
}