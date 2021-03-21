package com.alimoradi.data.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "song_history",
    indices = [(Index("id"))]
)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val songId: Long,
    val dateAdded: Long = System.currentTimeMillis()
)