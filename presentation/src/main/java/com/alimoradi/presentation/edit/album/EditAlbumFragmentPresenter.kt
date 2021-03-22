package com.alimoradi.presentation.edit.album

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.LastFmAlbum
import com.alimoradi.core.entity.track.Album
import com.alimoradi.core.gateway.ImageRetrieverGateway
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.podcast.PodcastAlbumGateway
import com.alimoradi.core.gateway.track.AlbumGateway
import com.alimoradi.core.interactor.songlist.GetSongListByParamUseCase
import com.alimoradi.intents.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EditAlbumFragmentPresenter @Inject constructor(
    private val albumGateway: AlbumGateway,
    private val podcastAlbumGateway: PodcastAlbumGateway,
    private val lastFmGateway: ImageRetrieverGateway,
    private val getSongListByParamUseCase: GetSongListByParamUseCase

) {

    fun getAlbum(mediaId: MediaId): Album {
        val album = if (mediaId.isPodcastAlbum) {
            podcastAlbumGateway.getByParam(mediaId.categoryId)!!
        } else {
            albumGateway.getByParam(mediaId.categoryId)!!
        }
        return Album(
            id = album.id,
            artistId = album.artistId,
            albumArtist = album.albumArtist,
            title = album.title,
            artist = if (album.artist == AppConstants.UNKNOWN) "" else album.artist,
            hasSameNameAsFolder = album.hasSameNameAsFolder,
            songs = album.songs,
            isPodcast = album.isPodcast
        )
    }

    suspend fun getPath(mediaId: MediaId): String = withContext(Dispatchers.IO) {
        getSongListByParamUseCase(mediaId).first().path
    }

    suspend fun fetchData(id: Id): LastFmAlbum? {
        return lastFmGateway.getAlbum(id)
    }

}