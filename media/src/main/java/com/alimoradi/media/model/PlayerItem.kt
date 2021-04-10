package com.alimoradi.media.model

import com.alimoradi.core.MediaId

data class PlayerItem(
    val mediaId: MediaId,
    val title: String,
    val artist: String,
    val idInPlaylist: Long
)