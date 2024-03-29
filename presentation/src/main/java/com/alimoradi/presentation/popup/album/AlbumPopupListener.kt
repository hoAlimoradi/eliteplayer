package com.alimoradi.presentation.popup.album

import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import com.alimoradi.appshortcuts.AppShortcuts
import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.track.Album
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.interactor.playlist.AddToPlaylistUseCase
import com.alimoradi.core.interactor.playlist.GetPlaylistsUseCase
import com.alimoradi.media.MediaProvider
import com.alimoradi.presentation.R
import com.alimoradi.presentation.navigator.Navigator
import com.alimoradi.presentation.popup.AbsPopup
import com.alimoradi.presentation.popup.AbsPopupListener
import java.lang.ref.WeakReference
import javax.inject.Inject

class AlbumPopupListener @Inject constructor(
    activity: FragmentActivity,
    private val navigator: Navigator,
    private val mediaProvider: MediaProvider,
    getPlaylistBlockingUseCase: GetPlaylistsUseCase,
    addToPlaylistUseCase: AddToPlaylistUseCase

) : AbsPopupListener(getPlaylistBlockingUseCase, addToPlaylistUseCase, false) {

    private val activityRef = WeakReference(activity)


    private lateinit var album: Album
    private var song: Song? = null

    fun setData(album: Album, song: Song?): AlbumPopupListener {
        this.album = album
        this.song = song
        return this
    }

    private fun getMediaId(): MediaId {
        if (song != null) {
            return MediaId.playableItem(album.getMediaId(), song!!.id)
        } else {
            return album.getMediaId()
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        val activity = activityRef.get() ?: return true


        val itemId = menuItem.itemId

        onPlaylistSubItemClick(activity, itemId, getMediaId(), album.songs, album.title)

        when (itemId) {
            AbsPopup.NEW_PLAYLIST_ID -> toCreatePlaylist()
            R.id.play -> playFromMediaId()
            R.id.playShuffle -> playShuffle()
            R.id.addToFavorite -> addToFavorite()
            R.id.playLater -> playLater()
            R.id.playNext -> playNext()
            R.id.delete -> delete()
            R.id.viewArtist -> viewArtist()
            R.id.viewAlbum -> viewAlbum(navigator, album.getMediaId())
            R.id.viewInfo -> viewInfo(navigator, getMediaId())
            R.id.share -> share(activity, song!!)
            R.id.setRingtone -> setRingtone(navigator, getMediaId(), song!!)
            R.id.addHomeScreen -> AppShortcuts.instance(activity).addDetailShortcut(
                getMediaId(),
                album.title
            )
        }

        return true
    }

    private fun toCreatePlaylist() {
        if (song == null) {
            navigator.toCreatePlaylistDialog(getMediaId(), album.songs, album.title)
        } else {
            navigator.toCreatePlaylistDialog(getMediaId(), -1, song!!.title)
        }
    }

    private fun playFromMediaId() {
        mediaProvider.playFromMediaId(getMediaId(), null, null)
    }

    private fun playShuffle() {
        mediaProvider.shuffle(getMediaId(), null)
    }

    private fun playLater() {
        if (song == null) {
            navigator.toPlayLater(getMediaId(), album.songs, album.title)
        } else {
            navigator.toPlayLater(getMediaId(), -1, song!!.title)
        }
    }

    private fun playNext() {
        if (song == null) {
            navigator.toPlayNext(getMediaId(), album.songs, album.title)
        } else {
            navigator.toPlayNext(getMediaId(), -1, song!!.title)
        }
    }


    private fun addToFavorite() {
        if (song == null) {
            navigator.toAddToFavoriteDialog(getMediaId(), album.songs, album.title)
        } else {
            navigator.toAddToFavoriteDialog(getMediaId(), -1, song!!.title)
        }
    }

    private fun delete() {
        if (song == null) {
            navigator.toDeleteDialog(getMediaId(), album.songs, album.title)
        } else {
            navigator.toDeleteDialog(getMediaId(), -1, song!!.title)
        }
    }

    private fun viewArtist() {
        navigator.toDetailFragment(album.getArtistMediaId())
    }


}