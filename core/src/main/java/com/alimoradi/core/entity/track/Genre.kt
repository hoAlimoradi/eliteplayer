package com.alimoradi.core.entity.track

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory

data class Genre(
    val id: Long,
    val name: String,
    val size: Int
) {

    fun getMediaId(): MediaId {
        return MediaId.createCategoryValue(MediaIdCategory.GENRES, id.toString())
    }

    fun withSongs(songs: Int): Genre {
        return Genre(
            id = id,
            name = name,
            size = songs
        )
    }

}