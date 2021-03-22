package com.alimoradi.data.mapper

import com.alimoradi.core.entity.LastFmAlbum
import com.alimoradi.core.entity.LastFmArtist
import com.alimoradi.core.entity.LastFmTrack
import com.alimoradi.data.db.entities.LastFmAlbumEntity
import com.alimoradi.data.db.entities.LastFmArtistEntity
import com.alimoradi.data.db.entities.LastFmTrackEntity
import java.text.SimpleDateFormat
import java.util.*

private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

private fun millisToFormattedDate(value: Long): String {
    return formatter.format(Date(value))

}

fun LastFmTrackEntity.toDomain(): LastFmTrack {
    return LastFmTrack(
        this.id,
        this.title,
        this.artist,
        this.album,
        this.image,
        this.mbid,
        this.artistMbid,
        this.albumMbid
    )
}

fun LastFmAlbumEntity.toDomain(): LastFmAlbum {
    return LastFmAlbum(
        this.id,
        this.title,
        this.artist,
        this.image,
        this.mbid,
        this.wiki
    )
}



fun LastFmTrack.toModel(): LastFmTrackEntity {
    return LastFmTrackEntity(
        this.id,
        this.title,
        this.artist,
        this.album,
        this.image,
        millisToFormattedDate(System.currentTimeMillis()),
        this.mbid,
        this.artistMbid,
        this.albumMbid
    )
}



fun LastFmAlbum.toModel(): LastFmAlbumEntity {
    return LastFmAlbumEntity(
        this.id,
        this.title,
        this.artist,
        this.image,
        millisToFormattedDate(System.currentTimeMillis()),
        this.mbid,
        this.wiki
    )
}

fun LastFmArtistEntity.toDomain(): LastFmArtist {
    return LastFmArtist(
        this.id,
        this.image,
        this.mbid,
        this.wiki
    )
}

fun LastFmArtist.toModel() : LastFmArtistEntity{
    return LastFmArtistEntity(
        this.id,
        this.image,
        millisToFormattedDate(System.currentTimeMillis()),
        this.mbid,
        this.wiki
    )
}

object LastFmNulls {

    fun createNullTrack(trackId: Long): LastFmTrackEntity {
        return LastFmTrackEntity(
            trackId,
            "",
            "",
            "",
            "",
            millisToFormattedDate(System.currentTimeMillis()),
            "",
            "",
            ""
        )
    }

    fun createNullArtist(artistId: Long): LastFmArtistEntity {
        return LastFmArtistEntity(
            artistId,
            "",
            millisToFormattedDate(System.currentTimeMillis()),
            "",
            ""
        )
    }

    fun createNullAlbum(albumId: Long): LastFmAlbumEntity {
        return LastFmAlbumEntity(
            albumId,
            "",
            "",
            "",
            millisToFormattedDate(System.currentTimeMillis()),
            "",
            ""
        )
    }

}
