package com.alimoradi.data.repository.track

import com.alimoradi.core.entity.track.Album
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.track.AlbumGateway
import com.alimoradi.data.repository.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class AlbumRepository @Inject constructor(): AlbumGateway {

    override fun observeArtistsAlbums(artistId: Id): Flow<List<Album>> {
        return observeAll()
    }

    override fun getAll(): List<Album> {
        return MockData.album(false)
    }

    override fun observeAll(): Flow<List<Album>> {
        return flowOf(getAll())
    }

    override fun getByParam(param: Id): Album? {
        return getAll().first()
    }

    override fun observeByParam(param: Id): Flow<Album?> {
        return flowOf(getByParam(param))
    }

    override fun getTrackListByParam(param: Id): List<Song> {
        return MockData.songs(false)
    }

    override fun observeTrackListByParam(param: Id): Flow<List<Song>> {
        return flowOf(getTrackListByParam(param))
    }

    override fun observeLastPlayed(): Flow<List<Album>> {
        return observeAll()
    }

    override suspend fun addLastPlayed(id: Id) {

    }

    override fun observeRecentlyAdded(): Flow<List<Album>> {
        return observeAll()
    }

    override fun observeSiblings(param: Id): Flow<List<Album>> {
        return observeAll()
    }
}