package com.alimoradi.presentation.popup.artist

import android.view.View
import com.alimoradi.core.entity.track.Artist
import com.alimoradi.core.entity.track.Song
import com.alimoradi.presentation.R
import com.alimoradi.presentation.popup.AbsPopup
import com.alimoradi.presentation.popup.AbsPopupListener
import com.alimoradi.sharedandroid.utils.isQ

class ArtistPopup(
    view: View,
    @Suppress("UNUSED_PARAMETER") artist: Artist,
    song: Song?,
    listener: AbsPopupListener

) : AbsPopup(view) {

    init {
        if (song == null) {
            inflate(R.menu.dialog_artist)
        } else {
            inflate(R.menu.dialog_song)
        }

        addPlaylistChooser(view.context, listener.playlists)

        setOnMenuItemClickListener(listener)

        if (isQ() && song == null) {
            // works bad on Q
            menu.removeItem(R.id.delete)
        }
    }

}