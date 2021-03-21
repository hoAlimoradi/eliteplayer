package com.alimoradi.presentation.popup.song

import android.view.View
import com.alimoradi.presentation.R
import com.alimoradi.presentation.popup.AbsPopup
import com.alimoradi.presentation.popup.AbsPopupListener

class SongPopup(
    view: View,
    listener: AbsPopupListener

) : AbsPopup(view) {

    init {
        inflate(R.menu.dialog_song)

        addPlaylistChooser(view.context, listener.playlists)

        setOnMenuItemClickListener(listener)
    }

}