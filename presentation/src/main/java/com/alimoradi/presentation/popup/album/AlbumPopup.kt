package com.alimoradi.presentation.popup.album

import android.view.View
import dev.olog.core.entity.track.Song
import com.alimoradi.presentation.R
import com.alimoradi.presentation.popup.AbsPopup
import com.alimoradi.presentation.popup.AbsPopupListener
import com.alimoradi.sharedandroid.utils.isQ

class AlbumPopup(
    view: View,
    song: Song?,
    listener: AbsPopupListener

) : AbsPopup(view) {

    init {
        if (song == null) {
            inflate(R.menu.dialog_album)
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