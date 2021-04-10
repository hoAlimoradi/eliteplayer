package com.alimoradi.core.entity.track

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory

data class Artist(
    val id: Long,
    val name: String,
    val albumArtist: String,
    val songs: Int,
    val isPodcast: Boolean
) {



    fun getMediaId(): MediaId {
        val category = if (isPodcast) MediaIdCategory.PODCASTS_ARTISTS else MediaIdCategory.ARTISTS
        return MediaId.createCategoryValue(category, this.id.toString())
    }

    fun withSongs(songs: Int): Artist {
        return Artist(
            id = id,
            name = name,
            albumArtist = albumArtist,
            songs = songs,
            isPodcast = isPodcast
        )
    }
}