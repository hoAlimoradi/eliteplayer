package com.alimoradi.core.entity.track

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory

data class Album(
    val id: Long,
    val artistId: Long,
    val title: String,
    val artist: String,
    val albumArtist: String,
    val songs: Int,
    val hasSameNameAsFolder: Boolean,
    val isPodcast: Boolean
) {


    fun getMediaId(): MediaId {
        val category = if (isPodcast) MediaIdCategory.PODCASTS_ALBUMS else MediaIdCategory.ALBUMS
        return MediaId.createCategoryValue(category, this.id.toString())
    }

    fun getArtistMediaId(): MediaId {
        val category = if (isPodcast) MediaIdCategory.PODCASTS_ARTISTS else MediaIdCategory.ARTISTS
        return MediaId.createCategoryValue(category, this.artistId.toString())
    }

    fun withSongs(songs: Int): Album {
        return Album(
            id = id,
            artistId = artistId,
            title = title,
            artist = artist,
            albumArtist = albumArtist,
            songs = songs,
            hasSameNameAsFolder = hasSameNameAsFolder,
            isPodcast = isPodcast
        )
    }

}