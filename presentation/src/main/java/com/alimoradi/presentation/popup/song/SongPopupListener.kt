package com.alimoradi.presentation.popup.song

import android.app.Activity
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.interactor.playlist.AddToPlaylistUseCase
import com.alimoradi.core.interactor.playlist.GetPlaylistsUseCase
import com.alimoradi.presentation.R
import com.alimoradi.presentation.navigator.Navigator
import com.alimoradi.presentation.popup.AbsPopup
import com.alimoradi.presentation.popup.AbsPopupListener
import java.lang.ref.WeakReference
import javax.inject.Inject

class SongPopupListener @Inject constructor(
    activity: FragmentActivity,
    private val navigator: Navigator,
    getPlaylistBlockingUseCase: GetPlaylistsUseCase,
    addToPlaylistUseCase: AddToPlaylistUseCase

) : AbsPopupListener(getPlaylistBlockingUseCase, addToPlaylistUseCase, false) {

    private val activityRef = WeakReference(activity)


    private lateinit var song: Song

    fun setData(song: Song): SongPopupListener {
        this.song = song
        return this
    }

    private fun getMediaId(): MediaId {
        return song.getMediaId()
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        val activity = activityRef.get() ?: return true

        val itemId = menuItem.itemId

        onPlaylistSubItemClick(activity, itemId, getMediaId(), -1, song.title)

        when (itemId) {
            AbsPopup.NEW_PLAYLIST_ID -> toCreatePlaylist()
            R.id.addToFavorite -> addToFavorite()
            R.id.playLater -> playLater()
            R.id.playNext -> playNext()
            R.id.delete -> delete()
            R.id.viewInfo -> viewInfo(navigator, getMediaId())
            R.id.viewAlbum -> viewAlbum(navigator, song.getAlbumMediaId())
            R.id.viewArtist -> viewArtist(navigator, song.getArtistMediaId())
            R.id.share -> share(activity, song)
            R.id.setRingtone -> setRingtone(navigator, getMediaId(), song)
        }


        return true
    }

    private fun toCreatePlaylist() {
        navigator.toCreatePlaylistDialog(getMediaId(), -1, song.title)
    }

    private fun playLater() {
        navigator.toPlayLater(getMediaId(), -1, song.title)
    }

    private fun playNext() {
        navigator.toPlayNext(getMediaId(), -1, song.title)
    }

    private fun addToFavorite() {
        navigator.toAddToFavoriteDialog(getMediaId(), -1, song.title)
    }

    private fun delete() {
        navigator.toDeleteDialog(getMediaId(), -1, song.title)
    }

}