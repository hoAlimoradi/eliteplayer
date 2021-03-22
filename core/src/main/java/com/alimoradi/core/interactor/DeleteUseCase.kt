package com.alimoradi.core.interactor

import com.alimoradi.core.MediaId
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.core.interactor.songlist.GetSongListByParamUseCase
import javax.inject.Inject

class DeleteUseCase @Inject constructor(
    private val playlistGateway: PlaylistGateway,
    private val podcastPlaylistGateway: PodcastPlaylistGateway,
    private val podcastGateway: PodcastGateway,
    private val songGateway: SongGateway,
    private val getSongListByParamUseCase: GetSongListByParamUseCase

) {

    suspend operator fun invoke(mediaId: MediaId) {
        if (mediaId.isLeaf && mediaId.isPodcast) {
            return podcastGateway.deleteSingle(mediaId.resolveId)
        }

        if (mediaId.isLeaf) {
            return songGateway.deleteSingle(mediaId.resolveId)
        }

        return when {
            mediaId.isPodcastPlaylist -> podcastPlaylistGateway.deletePlaylist(mediaId.categoryId)
            mediaId.isPlaylist -> playlistGateway.deletePlaylist(mediaId.categoryId)
            else -> {
                val songList = getSongListByParamUseCase(mediaId)
                if (mediaId.isAnyPodcast){
                    podcastGateway.deleteGroup(songList)
                } else {
                    songGateway.deleteGroup(songList)
                }
            }
        }
    }
}