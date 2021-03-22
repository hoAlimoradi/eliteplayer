package com.alimoradi.data.repository.track

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.track.Artist
import com.alimoradi.core.entity.track.Genre
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.track.GenreGateway
import com.alimoradi.data.repository.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GenreRepository @Inject constructor(): GenreGateway {

    override fun getAll(): List<Genre> {
        return MockData.genre()
    }

    override fun observeAll(): Flow<List<Genre>> {
        return flowOf(getAll())
    }

    override fun getByParam(param: Id): Genre? {
        return MockData.genre().first()
    }

    override fun observeByParam(param: Id): Flow<Genre?> {
        return flowOf(getByParam(param))
    }

    override fun getTrackListByParam(param: Id): List<Song> {
        return MockData.songs(false)
    }

    override fun observeTrackListByParam(param: Id): Flow<List<Song>> {
        return flowOf(getTrackListByParam(param))
    }

    override fun observeMostPlayed(mediaId: MediaId): Flow<List<Song>> {
        return observeTrackListByParam(mediaId.resolveId)
    }

    override suspend fun insertMostPlayed(mediaId: MediaId) {

    }

    override fun observeSiblings(param: Id): Flow<List<Genre>> {
        return observeAll()
    }

    override fun observeRelatedArtists(params: Id): Flow<List<Artist>> {
        return flowOf(MockData.artist(false))
    }

    override fun observeRecentlyAdded(path: Id): Flow<List<Song>> {
        return observeTrackListByParam(path)
    }
}