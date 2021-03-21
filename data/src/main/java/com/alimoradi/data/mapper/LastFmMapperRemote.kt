package com.alimoradi.data.mapper

import dev.olog.core.entity.LastFmAlbum
import dev.olog.core.entity.LastFmArtist
import dev.olog.core.entity.LastFmTrack
import com.alimoradi.data.api.lastfm.album.AlbumInfo
import com.alimoradi.data.api.lastfm.album.AlbumSearch
import com.alimoradi.data.api.lastfm.artist.ArtistInfo
import com.alimoradi.data.api.lastfm.track.TrackInfo
import com.alimoradi.data.api.lastfm.track.TrackSearch
import me.xdrop.fuzzywuzzy.FuzzySearch

fun TrackInfo.toDomain(id: Long): LastFmTrack {
    val track = this.track
    val title = track.name
    val artist = track.artist.name
    val album = track.album.title
    val image = track.album.image.reversed().find { it.text.isNotBlank() }!!.text

    return LastFmTrack(
        id,
        title,
        artist,
        album,
        image,
        track.mbid ?: "",
        track.artist.mbid ?: "",
        track.album.mbid ?: ""
    )
}

fun TrackSearch.toDomain(id: Long): LastFmTrack? {
    try {
        val track = this.results.trackmatches.track[0]

        return LastFmTrack(
            id,
            track.name ?: "",
            track.artist ?: "",
            "",
            "",
            "",
            "",
            ""
        )
    } catch (ex: Throwable) {
        ex.printStackTrace()
        return LastFmTrack(
            id,
            "",
            "",
            "",
            "",
            "",
            "",
            ""
        )
    }
}

fun AlbumInfo.toDomain(id: Long): LastFmAlbum {
    val album = this.album
    return LastFmAlbum(
        id,
        album.name,
        album.artist,
        album.image.reversed().find { it.text.isNotBlank() }?.text!!,
        album.mbid ?: "",
        album.wiki.content ?: ""
    )
}

fun AlbumSearch.toDomain(id: Long, originalArtist: String): LastFmAlbum {
    try {
        val results = this.results.albummatches.album
        val bestArtist = FuzzySearch.extractOne(originalArtist, results.map { it.artist }).string
        val best = results.first { it.artist == bestArtist }

        return LastFmAlbum(
            id,
            best.name,
            best.artist,
            "",
            "",
            ""
        )
    } catch (ex: Throwable) {
        ex.printStackTrace()
        return LastFmAlbum(
            id,
            "",
            "",
            "",
            "",
            ""
        )
    }
}

fun ArtistInfo.toDomain(id: Long): LastFmArtist? {
    val artist = this.artist
    return LastFmArtist(
        id,
        "",
        artist.mbid ?: "",
        artist.bio.content ?: ""
    )
}