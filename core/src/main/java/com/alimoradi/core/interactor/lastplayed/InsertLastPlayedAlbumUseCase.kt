package com.alimoradi.core.interactor.lastplayed

import com.alimoradi.core.MediaId
import com.alimoradi.core.gateway.track.AlbumGateway
import com.alimoradi.core.gateway.podcast.PodcastAlbumGateway
import javax.inject.Inject

class InsertLastPlayedAlbumUseCase @Inject constructor(
    private val albumGateway: AlbumGateway,
    private val podcastGateway: PodcastAlbumGateway

) {

    suspend operator fun invoke(mediaId: MediaId) {
        if (mediaId.isPodcastAlbum) {
            podcastGateway.addLastPlayed(mediaId.categoryValue.toLong())
        } else {
            albumGateway.addLastPlayed(mediaId.categoryValue.toLong())
        }
    }

}