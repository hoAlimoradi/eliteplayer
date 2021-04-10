package com.alimoradi.presentation.popup

import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.gateway.podcast.PodcastAlbumGateway
import com.alimoradi.core.gateway.podcast.PodcastArtistGateway
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.*
import com.alimoradi.presentation.popup.album.AlbumPopup
import com.alimoradi.presentation.popup.artist.ArtistPopup
import com.alimoradi.presentation.popup.folder.FolderPopup
import com.alimoradi.presentation.popup.genre.GenrePopup
import com.alimoradi.presentation.popup.playlist.PlaylistPopup
import com.alimoradi.presentation.popup.song.SongPopup
import javax.inject.Inject

class PopupMenuFactory @Inject constructor(
    private val getFolderUseCase: FolderGateway,
    private val getPlaylistUseCase: PlaylistGateway,
    private val getSongUseCase: SongGateway,
    private val getAlbumUseCase: AlbumGateway,
    private val getArtistUseCase: ArtistGateway,
    private val getGenreUseCase: GenreGateway,
    private val getPodcastUseCase: PodcastGateway,
    private val getPodcastPlaylistUseCase: PodcastPlaylistGateway,
    private val getPodcastAlbumUseCase: PodcastAlbumGateway,
    private val getPodcastArtistUseCase: PodcastArtistGateway,
    private val listenerFactory: MenuListenerFactory

) {

    fun create(view: View, mediaId: MediaId): PopupMenu {
        val category = mediaId.category
        return when (category) {
            MediaIdCategory.FOLDERS -> getFolderPopup(view, mediaId)
            MediaIdCategory.PLAYLISTS -> getPlaylistPopup(view, mediaId)
            MediaIdCategory.SONGS -> getSongPopup(view, mediaId)
            MediaIdCategory.ALBUMS -> getAlbumPopup(view, mediaId)
            MediaIdCategory.ARTISTS -> getArtistPopup(view, mediaId)
            MediaIdCategory.GENRES -> getGenrePopup(view, mediaId)
            MediaIdCategory.PODCASTS -> getPodcastPopup(view, mediaId)
            MediaIdCategory.PODCASTS_PLAYLIST -> getPodcastPlaylistPopup(view, mediaId)
            MediaIdCategory.PODCASTS_ALBUMS -> getPodcastAlbumPopup(view, mediaId)
            MediaIdCategory.PODCASTS_ARTISTS -> getPodcastArtistPopup(view, mediaId)
            else -> throw IllegalArgumentException("invalid category $category")
        }
    }

    private fun getFolderPopup(view: View, mediaId: MediaId): PopupMenu {
        val folder = getFolderUseCase.getByParam(mediaId.categoryValue)!!
        return if (mediaId.isLeaf) {
            val song = getSongUseCase.getByParam(mediaId.leaf!!)
            FolderPopup(view, folder, song, listenerFactory.folder(folder, song))
        } else {
            FolderPopup(view, folder, null, listenerFactory.folder(folder, null))
        }
    }

    private fun getPlaylistPopup(view: View, mediaId: MediaId): PopupMenu {
        val playlist = getPlaylistUseCase.getByParam(mediaId.categoryId)!!
        return if (mediaId.isLeaf) {
            val song = getSongUseCase.getByParam(mediaId.leaf!!)
            PlaylistPopup(view, playlist, song, listenerFactory.playlist(playlist, song))
        } else {
            PlaylistPopup(view, playlist, null, listenerFactory.playlist(playlist, null))
        }
    }

    private fun getSongPopup(view: View, mediaId: MediaId): PopupMenu {
        val song = getSongUseCase.getByParam(mediaId.leaf!!)!!
        return SongPopup(view, listenerFactory.song(song))
    }

    private fun getAlbumPopup(view: View, mediaId: MediaId): PopupMenu {
        val album = getAlbumUseCase.getByParam(mediaId.categoryId)!!
        return if (mediaId.isLeaf) {
            val song = getSongUseCase.getByParam(mediaId.leaf!!)
            AlbumPopup(view, song, listenerFactory.album(album, song))
        } else {
            AlbumPopup(view, null, listenerFactory.album(album, null))
        }
    }

    private fun getArtistPopup(view: View, mediaId: MediaId): PopupMenu {
        val artist = getArtistUseCase.getByParam(mediaId.categoryId)!!
        return if (mediaId.isLeaf) {
            val song = getSongUseCase.getByParam(mediaId.leaf!!)
            ArtistPopup(view, artist, song, listenerFactory.artist(artist, song))
        } else {
            ArtistPopup(view, artist, null, listenerFactory.artist(artist, null))
        }
    }

    private fun getGenrePopup(view: View, mediaId: MediaId): PopupMenu {
        val genre = getGenreUseCase.getByParam(mediaId.categoryId)!!
        return if (mediaId.isLeaf) {
            val song = getSongUseCase.getByParam(mediaId.leaf!!)
            GenrePopup(view, genre, song, listenerFactory.genre(genre, song))
        } else {
            GenrePopup(view, genre, null, listenerFactory.genre(genre, null))
        }
    }

    private fun getPodcastPopup(view: View, mediaId: MediaId): PopupMenu {
        val song = getPodcastUseCase.getByParam(mediaId.leaf!!)!!
        return SongPopup(view, listenerFactory.song(song))
    }

    private fun getPodcastPlaylistPopup(view: View, mediaId: MediaId): PopupMenu {
        val playlist = getPodcastPlaylistUseCase.getByParam(mediaId.categoryId)!!
        return if (mediaId.isLeaf) {
            val song = getSongUseCase.getByParam(mediaId.leaf!!)
            PlaylistPopup(view, playlist, song, listenerFactory.playlist(playlist, song))
        } else {
            PlaylistPopup(view, playlist, null, listenerFactory.playlist(playlist, null))
        }
    }

    private fun getPodcastAlbumPopup(view: View, mediaId: MediaId): PopupMenu {
        val album = getPodcastAlbumUseCase.getByParam(mediaId.categoryId)!!
        return if (mediaId.isLeaf) {
            val song = getSongUseCase.getByParam(mediaId.leaf!!)
            AlbumPopup(view, song, listenerFactory.album(album, song))
        } else {
            AlbumPopup(view, null, listenerFactory.album(album, null))
        }
    }

    private fun getPodcastArtistPopup(view: View, mediaId: MediaId): PopupMenu {
        val artist = getPodcastArtistUseCase.getByParam(mediaId.categoryId)!!
        return if (mediaId.isLeaf) {
            val song = getSongUseCase.getByParam(mediaId.leaf!!)
            ArtistPopup(view, artist, song, listenerFactory.artist(artist, song))
        } else {
            ArtistPopup(view, artist, null, listenerFactory.artist(artist, null))
        }
    }

}