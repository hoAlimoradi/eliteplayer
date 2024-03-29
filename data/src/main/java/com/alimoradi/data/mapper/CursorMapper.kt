@file:Suppress("DEPRECATION")

package com.alimoradi.data.mapper

import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import com.alimoradi.core.entity.track.*
import com.alimoradi.data.queries.Columns
import com.alimoradi.data.utils.getInt
import com.alimoradi.data.utils.getLong
import com.alimoradi.data.utils.getStringOrNull
import java.io.File

fun Cursor.toSong(): Song {
    val id = getLong(BaseColumns._ID)
    val artistId = getLong(MediaStore.Audio.AudioColumns.ARTIST_ID)
    val albumId = getLong(MediaStore.Audio.AudioColumns.ALBUM_ID)

    val path = getStringOrNull(MediaStore.MediaColumns.DATA) ?: ""

    val title = getStringOrNull(MediaStore.MediaColumns.TITLE) ?: ""

    val artist = getStringOrNull(MediaStore.Audio.AudioColumns.ARTIST) ?: ""
    val album = getStringOrNull(MediaStore.Audio.AudioColumns.ALBUM) ?: ""

    val albumArtist = getStringOrNull(Columns.ALBUM_ARTIST) ?: artist

    val duration = getLong(MediaStore.Audio.AudioColumns.DURATION)
    val dateAdded = getLong(MediaStore.MediaColumns.DATE_ADDED)
    val dateModified = getLong(MediaStore.MediaColumns.DATE_MODIFIED)

    val track = getInt(MediaStore.Audio.AudioColumns.TRACK)
    val isPodcast = getLong(MediaStore.Audio.AudioColumns.IS_PODCAST) != 0L

    return Song(
        id = id,
        artistId = artistId,
        albumId = albumId,
        title = title,
        artist = artist,
        albumArtist = albumArtist,
        album = album,
        duration = duration,
        dateAdded = dateAdded,
        dateModified = dateModified,
        path = path,
        trackColumn = track,
        idInPlaylist = -1,
        isPodcast = isPodcast
    )
}

fun Cursor.toPlaylistSong(): Song {
    val idInPlaylist = getInt(MediaStore.Audio.Playlists.Members._ID)
    val id = getLong(MediaStore.Audio.Playlists.Members.AUDIO_ID)
    val artistId = getLong(MediaStore.Audio.AudioColumns.ARTIST_ID)
    val albumId = getLong(MediaStore.Audio.AudioColumns.ALBUM_ID)

    val path = getStringOrNull(MediaStore.MediaColumns.DATA) ?: ""

    val title = getStringOrNull(MediaStore.MediaColumns.TITLE) ?: ""

    val artist = getStringOrNull(MediaStore.Audio.AudioColumns.ARTIST) ?: ""
    val album = getStringOrNull(MediaStore.Audio.AudioColumns.ALBUM) ?: ""

    val albumArtist = getStringOrNull(Columns.ALBUM_ARTIST) ?: artist

    val duration = getLong(MediaStore.Audio.AudioColumns.DURATION)
    val dateAdded = getLong(MediaStore.MediaColumns.DATE_ADDED)
    val dateModified = getLong(MediaStore.MediaColumns.DATE_MODIFIED)

    val track = getInt(MediaStore.Audio.AudioColumns.TRACK)
    val isPodcast = getLong(MediaStore.Audio.AudioColumns.IS_PODCAST) != 0L

    return Song(
        id = id,
        artistId = artistId,
        albumId = albumId,
        title = title,
        artist = artist,
        albumArtist = albumArtist,
        album = album,
        duration = duration,
        dateAdded = dateAdded,
        dateModified = dateModified,
        path = path,
        trackColumn = track,
        idInPlaylist = idInPlaylist,
        isPodcast = isPodcast
    )
}

fun Cursor.toAlbum(): Album {
    val title = getStringOrNull(MediaStore.Audio.Media.ALBUM) ?: ""
    val artist = getStringOrNull(MediaStore.Audio.Media.ARTIST) ?: ""
    val albumArtist = getStringOrNull(Columns.ALBUM_ARTIST) ?: artist

    val dirName = try {
        val data = getStringOrNull(MediaStore.Audio.AudioColumns.DATA) ?: ""
        val path = data.substring(1, data.lastIndexOf(File.separator))
        path.substring(path.lastIndexOf(File.separator) + 1)
    } catch (ex: Throwable){
        ex.printStackTrace()
        ""
    }
    val isPodcast = getLong(MediaStore.Audio.AudioColumns.IS_PODCAST) != 0L

    return Album(
        id = getLong(MediaStore.Audio.Media.ALBUM_ID),
        artistId = getLong(MediaStore.Audio.Media.ARTIST_ID),
        title = title,
        artist = artist,
        albumArtist = albumArtist,
        songs = 0,
        hasSameNameAsFolder = dirName == title,
        isPodcast = isPodcast
    )
}

fun Cursor.toArtist(): Artist {
    val artist = getStringOrNull(MediaStore.Audio.Media.ARTIST) ?: ""
    val albumArtist = getStringOrNull(Columns.ALBUM_ARTIST) ?: artist
    val isPodcast = getLong(MediaStore.Audio.AudioColumns.IS_PODCAST) != 0L

    return Artist(
        id = getLong(MediaStore.Audio.Media.ARTIST_ID),
        name = artist,
        albumArtist = albumArtist,
        songs = 0,
        isPodcast = isPodcast
    )
}

internal fun Cursor.toGenre(): Genre {
    val id = this.getLong(BaseColumns._ID)
    val name = this.getStringOrNull(MediaStore.Audio.GenresColumns.NAME)?.capitalize() ?: ""
    return Genre(
        id = id,
        name = name,
        size = 0 // wil be updated later
    )
}