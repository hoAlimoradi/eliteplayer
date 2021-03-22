package com.alimoradi.presentation.popup.folder

import android.view.View
import com.alimoradi.core.entity.track.Folder
import com.alimoradi.core.entity.track.Song
import com.alimoradi.presentation.R
import com.alimoradi.presentation.popup.AbsPopup
import com.alimoradi.presentation.popup.AbsPopupListener
import com.alimoradi.sharedandroid.utils.isQ

class FolderPopup(
    view: View,
    @Suppress("UNUSED_PARAMETER") folder: Folder,
    song: Song?,
    listener: AbsPopupListener

) : AbsPopup(view) {


    init {
        if (song == null) {
            inflate(R.menu.dialog_folder)
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