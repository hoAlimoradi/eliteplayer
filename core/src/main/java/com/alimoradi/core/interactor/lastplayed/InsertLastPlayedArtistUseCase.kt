package com.alimoradi.core.interactor.lastplayed

import com.alimoradi.core.MediaId
import com.alimoradi.core.gateway.track.ArtistGateway
import com.alimoradi.core.gateway.podcast.PodcastArtistGateway
import javax.inject.Inject

class InsertLastPlayedArtistUseCase @Inject constructor(
    private val artistGateway: ArtistGateway,
    private val podcastGateway: PodcastArtistGateway

) {

    suspend operator fun invoke(mediaId: MediaId) {
        if (mediaId.isPodcastArtist) {
            podcastGateway.addLastPlayed(mediaId.categoryValue.toLong())
        } else {
            artistGateway.addLastPlayed(mediaId.categoryValue.toLong())
        }
    }
}