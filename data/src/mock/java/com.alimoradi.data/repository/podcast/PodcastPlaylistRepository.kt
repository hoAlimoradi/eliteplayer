package com.alimoradi.data.repository.podcast

import dev.olog.core.entity.track.Artist
import dev.olog.core.entity.track.Playlist
import dev.olog.core.entity.track.Song
import dev.olog.core.gateway.base.Id
import dev.olog.core.gateway.podcast.PodcastGateway
import dev.olog.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.data.repository.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PodcastPlaylistRepository @Inject constructor(): PodcastPlaylistGateway {

    override suspend fun renamePlaylist(playlistId: Id, newTitle: String) {

    }

    override suspend fun deletePlaylist(playlistId: Id) {

    }

    override suspend fun clearPlaylist(playlistId: Id) {

    }



    override suspend fun removeFromPlaylist(playlistId: Id, idInPlaylist: Long) {

    }

    override suspend fun removeDuplicated(playlistId: Id) {

    }

    override suspend fun insertPodcastToHistory(podcastId: Id) {

    }

    override fun getAll(): List<Playlist> {
        return MockData.playlist(true)
    }

    override fun observeAll(): Flow<List<Playlist>> {
        return flowOf(getAll())
    }

    override fun getByParam(param: Id): Playlist? {
        return getAll().first()
    }

    override fun observeByParam(param: Id): Flow<Playlist?> {
        return flowOf(getByParam(param))
    }

    override fun getTrackListByParam(param: Id): List<Song> {
        return MockData.songs(true)
    }

    override fun observeTrackListByParam(param: Id): Flow<List<Song>> {
        return flowOf(getTrackListByParam(param))
    }

    override fun observeSiblings(param: Id): Flow<List<Playlist>> {
        return observeAll()
    }

    override fun observeRelatedArtists(params: Id): Flow<List<Artist>> {
        return flowOf(MockData.artist(true))
    }

    override fun getAllAutoPlaylists(): List<Playlist> {
        return MockData.autoPlaylist()
    }

    override suspend fun createPlaylist(playlistName: String): Long {
        return 1
    }

    override suspend fun addSongsToPlaylist(playlistId: Id, songIds: List<Long>) {

    }

    override suspend fun moveItem(playlistId: Long, moveList: List<Pair<Int, Int>>) {

    }
}