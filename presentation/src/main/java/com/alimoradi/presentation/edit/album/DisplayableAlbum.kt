package com.alimoradi.presentation.edit.album

data class DisplayableAlbum(
    val id: Long,
    val title: String,
    val artist: String,
    val albumArtist: String,
    val genre: String,
    val year: String,
    val songs: Int,
    val isPodcast: Boolean
)