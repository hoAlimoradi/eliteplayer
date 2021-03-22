package com.alimoradi.core.entity.track

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory

data class Playlist(
    val id: Long,
    val title: String,
    val size: Int,
    val isPodcast: Boolean
) {

    fun getMediaId(): MediaId {
        val category =
            if (isPodcast) MediaIdCategory.PODCASTS_PLAYLIST else MediaIdCategory.PLAYLISTS
        return MediaId.createCategoryValue(category, id.toString())
    }

    fun withSongs(songs: Int): Playlist {
        return Playlist(
            id = id,
            title = title,
            size = songs,
            isPodcast = isPodcast
        )
    }

}