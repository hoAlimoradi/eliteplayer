package com.alimoradi.presentation.detail.mapper

import android.content.res.Resources
import com.alimoradi.core.entity.AutoPlaylist
import com.alimoradi.core.entity.track.*
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.DisplayableHeader


internal fun Folder.toHeaderItem(resources: Resources): DisplayableHeader {

    return DisplayableHeader(
        type = R.layout.item_detail_image,
        mediaId = getMediaId(),
        title = title,
        subtitle = resources.getQuantityString(
            R.plurals.common_plurals_song,
            this.size,
            this.size
        ).toLowerCase()
    )
}

internal fun Playlist.toHeaderItem(resources: Resources): DisplayableHeader {
    val subtitle = if (AutoPlaylist.isAutoPlaylist(id)){
        ""
    } else {
        resources.getQuantityString(R.plurals.common_plurals_song, this.size, this.size).toLowerCase()
    }

    return DisplayableHeader(
        type = R.layout.item_detail_image,
        mediaId = getMediaId(),
        title = title,
        subtitle = subtitle
    )

}

internal fun Album.toHeaderItem(): DisplayableHeader {

    return DisplayableHeader(
        type = R.layout.item_detail_image,
        mediaId = getMediaId(),
        title = title,
        subtitle = this.artist
    )
}

internal fun Artist.toHeaderItem(resources: Resources): DisplayableHeader {

    return DisplayableHeader(
        type = R.layout.item_detail_image,
        mediaId = getMediaId(),
        title = name,
        subtitle = resources.getQuantityString(R.plurals.common_plurals_song, this.songs, this.songs).toLowerCase()
    )
}

internal fun Genre.toHeaderItem(resources: Resources): DisplayableHeader {

    return DisplayableHeader(
        type = R.layout.item_detail_image,
        mediaId = getMediaId(),
        title = name,
        subtitle = resources.getQuantityString(
            R.plurals.common_plurals_song,
            this.size,
            this.size
        ).toLowerCase()
    )
}