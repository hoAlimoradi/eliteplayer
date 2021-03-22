package com.alimoradi.presentation.edit.artist

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.LastFmArtist
import com.alimoradi.core.entity.track.Artist
import com.alimoradi.core.gateway.ImageRetrieverGateway
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.podcast.PodcastArtistGateway
import com.alimoradi.core.gateway.track.ArtistGateway
import javax.inject.Inject

class EditArtistFragmentPresenter @Inject constructor(
    private val artistGateway: ArtistGateway,
    private val podcastArtistGateway: PodcastArtistGateway,
    private val lastFmGateway: ImageRetrieverGateway

) {

    fun getArtist(mediaId: MediaId): Artist {
        val artist = if (mediaId.isPodcastArtist) {
            podcastArtistGateway.getByParam(mediaId.categoryId)!!
        } else {
            artistGateway.getByParam(mediaId.categoryId)!!
        }
        return Artist(
            id = artist.id,
            name = artist.name,
            albumArtist = artist.albumArtist,
            songs = artist.songs,
            isPodcast = artist.isPodcast
        )
    }

    suspend fun fetchData(id: Id): LastFmArtist? {
        return lastFmGateway.getArtist(id)
    }

}