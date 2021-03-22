package com.alimoradi.core.entity

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.track.Song

data class PlayingQueueSong(
    val song: Song,
    val mediaId: MediaId
)