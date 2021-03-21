package com.alimoradi.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "podcast_playlist")
data class PodcastPlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val size: Int
)

@Entity(
    tableName = "podcast_playlist_tracks",
    indices = [Index("playlistId")],
    foreignKeys = [
        ForeignKey(
            entity = PodcastPlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PodcastPlaylistTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // progressive
    val idInPlaylist: Long,
    val podcastId: Long,
    val playlistId: Long
)