package com.alimoradi.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcast_position")
data class PodcastPositionEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val position: Long
)