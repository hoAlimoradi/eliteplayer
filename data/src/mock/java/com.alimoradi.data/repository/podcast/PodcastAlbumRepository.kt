package com.alimoradi.data.repository.podcast

import com.alimoradi.core.entity.track.Album
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.podcast.PodcastAlbumGateway
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.data.repository.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PodcastAlbumRepository @Inject constructor(): PodcastAlbumGateway {

    override fun observeArtistsAlbums(artistId: Id): Flow<List<Album>> {
        return flowOf(MockData.album(true))
    }

    override fun getAll(): List<Album> {
        return MockData.album(true)
    }

    override fun observeAll(): Flow<List<Album>> {
        return flowOf(getAll())
    }

    override fun getByParam(param: Id): Album? {
        return MockData.album(true).first()
    }

    override fun observeByParam(param: Id): Flow<Album?> {
        return flowOf(getByParam(param))
    }

    override fun observeLastPlayed(): Flow<List<Album>> {
        return observeAll()
    }

    override suspend fun addLastPlayed(id: Id) {

    }

    override fun observeRecentlyAdded(): Flow<List<Album>> {
        return observeAll()
    }

    override fun getTrackListByParam(param: Id): List<Song> {
        return MockData.songs(true)
    }

    override fun observeTrackListByParam(param: Id): Flow<List<Song>> {
        return flowOf(getTrackListByParam(param))
    }

    override fun observeSiblings(param: Id): Flow<List<Album>> {
        return observeAll()
    }
}