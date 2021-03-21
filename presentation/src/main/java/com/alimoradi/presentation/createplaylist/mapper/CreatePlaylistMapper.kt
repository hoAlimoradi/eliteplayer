package com.alimoradi.presentation.createplaylist.mapper

import dev.olog.core.entity.track.Song
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.DisplayableTrack

internal fun Song.toDisplayableItem(): DisplayableTrack {
    return DisplayableTrack(
        type = R.layout.item_create_playlist,
        mediaId = getMediaId(),
        title = this.title,
        artist = this.artist,
        album = this.album,
        idInPlaylist = this.idInPlaylist,
        dataModified = this.dateModified
    )
}