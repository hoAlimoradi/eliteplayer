package com.alimoradi.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.alimoradi.data.db.entities.LastPlayedArtistEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class LastPlayedArtistDao {

    @Query(
        """
        SELECT * FROM last_played_artists
        ORDER BY dateAdded DESC
        LIMIT 20
    """
    )
    abstract fun getAll(): Flow<List<LastPlayedArtistEntity>>

    @Insert
    internal abstract suspend fun insertImpl(entity: LastPlayedArtistEntity)

    @Query(
        """
        DELETE FROM last_played_artists
        WHERE id = :artistId
    """
    )
    internal abstract suspend fun deleteImpl(artistId: Long)

    @Transaction
    open suspend fun insertOne(id: Long) {
        deleteImpl(id)
        insertImpl(LastPlayedArtistEntity(id))
    }

}
