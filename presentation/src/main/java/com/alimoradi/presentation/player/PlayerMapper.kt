package com.alimoradi.presentation.player

import com.alimoradi.media.model.PlayerItem
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.DisplayableItem
import com.alimoradi.presentation.model.DisplayableTrack

internal fun PlayerItem.toDisplayableItem(): DisplayableItem {
    return DisplayableTrack(
        type = R.layout.item_mini_queue,
        mediaId = mediaId,
        title = title,
        artist = artist,
        album = "",
        idInPlaylist = idInPlaylist.toInt(),
        dataModified = -1
    )
}