package com.alimoradi.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.data.db.entities.PlaylistMostPlayedEntity
import com.alimoradi.data.db.entities.SongMostTimesPlayedEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
internal abstract class PlaylistMostPlayedDao {

    @Query(
        """
        SELECT songId, count(*) as timesPlayed
        FROM most_played_playlist
        WHERE playlistId = :playlistId
        GROUP BY songId
        HAVING count(*) >= 5
        ORDER BY timesPlayed DESC
        LIMIT 10
    """
    )
    abstract fun query(playlistId: Long): Flow<List<SongMostTimesPlayedEntity>>

    @Insert
    abstract fun insertOne(item: PlaylistMostPlayedEntity)

    fun getAll(playlistId: Long, songGateway2: SongGateway): Flow<List<Song>> {
        return this.query(playlistId)
            .map { mostPlayed ->
                val songList = songGateway2.getAll()
                mostPlayed.sortedByDescending { it.timesPlayed }
                    .mapNotNull { item -> songList.find { it.id == item.songId } }
            }
    }

}
