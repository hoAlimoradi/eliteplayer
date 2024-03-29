package com.alimoradi.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.data.db.entities.FolderMostPlayedEntity
import com.alimoradi.data.db.entities.SongMostTimesPlayedEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
internal abstract class FolderMostPlayedDao {

    @Query(
        """
        SELECT songId, count(*) as timesPlayed
        FROM most_played_folder
        WHERE folderPath = :folderPath
        GROUP BY songId
        HAVING count(*) >= 5
        ORDER BY timesPlayed DESC
        LIMIT 10
    """
    )
    abstract fun query(folderPath: String): Flow<List<SongMostTimesPlayedEntity>>

    @Insert
    abstract suspend fun insertOne(item: FolderMostPlayedEntity)

    fun getAll(folderPath: String, songGateway2: SongGateway): Flow<List<Song>> {
        return this.query(folderPath)
            .map { mostPlayed ->
                val songList = songGateway2.getAll()
                mostPlayed.sortedByDescending { it.timesPlayed }
                    .mapNotNull { item -> songList.find { it.id == item.songId } }
            }
    }

}
