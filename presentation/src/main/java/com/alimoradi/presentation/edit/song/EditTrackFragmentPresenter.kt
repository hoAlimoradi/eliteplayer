package com.alimoradi.presentation.edit.song

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.LastFmTrack
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.ImageRetrieverGateway
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.intents.AppConstants
import javax.inject.Inject

class EditTrackFragmentPresenter @Inject constructor(
    private val songGateway: SongGateway,
    private val podcastGateway: PodcastGateway,
    private val lastFmGateway: ImageRetrieverGateway

) {

    fun getSong(mediaId: MediaId): Song {
        val song = if (mediaId.isPodcast) {
            podcastGateway.getByParam(mediaId.leaf!!)!!
        } else {
            songGateway.getByParam(mediaId.leaf!!)!!
        }
        return song.copy(
            artist = if (song.artist == AppConstants.UNKNOWN) "" else song.artist,
            album = if (song.album == AppConstants.UNKNOWN) "" else song.album
        )
    }

    suspend fun fetchData(id: Id): LastFmTrack? {
        return lastFmGateway.getTrack(id)
    }

}