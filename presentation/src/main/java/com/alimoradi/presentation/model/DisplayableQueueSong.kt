package com.alimoradi.presentation.model

import com.alimoradi.core.MediaId
import com.alimoradi.shared.TextUtils

data class DisplayableQueueSong(
    override val type: Int,
    override val mediaId: MediaId,
    val title: String,
    val artist: String,
    val album: String,
    val idInPlaylist: Int,
    val relativePosition: String,
    val isCurrentSong: Boolean

) : BaseModel {

    val subtitle = "$artist${TextUtils.MIDDLE_DOT_SPACED}$album"

}