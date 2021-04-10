package com.alimoradi.data.repository.track

import android.net.Uri
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.data.repository.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SongRepository @Inject constructor(

) : SongGateway {

    override suspend fun deleteSingle(id: Id) {

    }

    override suspend fun deleteGroup(ids: List<Song>) {

    }

    override fun getByUri(uri: Uri): Song? {
        return null
    }

    override fun getByAlbumId(albumId: Id): Song? {
        return getAll().find { it.albumId == albumId }
    }

    override fun getAll(): List<Song> {
        return MockData.songs(false)
    }

    override fun observeAll(): Flow<List<Song>> {
        return flowOf(getAll())
    }

    override fun getByParam(param: Id): Song? {
        return getAll().find { it.id == param }
    }

    override fun observeByParam(param: Id): Flow<Song?> {
        return flowOf(getByParam(param))
    }
}