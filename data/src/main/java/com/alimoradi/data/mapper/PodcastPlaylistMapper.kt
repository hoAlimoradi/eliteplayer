@file:Suppress("NOTHING_TO_INLINE")

package com.alimoradi.data.mapper

import com.alimoradi.core.entity.track.Playlist
import com.alimoradi.data.db.entities.PlaylistEntity
import com.alimoradi.data.db.entities.PodcastPlaylistEntity

internal inline fun PodcastPlaylistEntity.toDomain(): Playlist {
    return Playlist(
        this.id,
        this.name,
        this.size,
        true
    )
}

internal inline fun PlaylistEntity.toDomain(): Playlist {
    return Playlist(
        this.id,
        this.name,
        this.size,
        false
    )
}