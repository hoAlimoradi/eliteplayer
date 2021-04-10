package com.alimoradi.presentation.popup.genre

import android.view.View
import com.alimoradi.core.entity.track.Genre
import com.alimoradi.core.entity.track.Song
import com.alimoradi.presentation.R
import com.alimoradi.presentation.popup.AbsPopup
import com.alimoradi.presentation.popup.AbsPopupListener
import com.alimoradi.sharedandroid.utils.isQ

class GenrePopup(
    view: View,
    @Suppress("UNUSED_PARAMETER") genre: Genre,
    song: Song?,
    listener: AbsPopupListener

) : AbsPopup(view) {

    init {
        if (song == null) {
            inflate(R.menu.dialog_genre)
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