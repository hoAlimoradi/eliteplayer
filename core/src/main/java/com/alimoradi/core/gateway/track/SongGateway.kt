package com.alimoradi.core.gateway.track

import android.net.Uri
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.BaseGateway
import com.alimoradi.core.gateway.base.Id

interface SongGateway :
    BaseGateway<Song, Id> {

    suspend fun deleteSingle(id: Id)
    suspend fun deleteGroup(ids: List<Song>)

    fun getByUri(uri: Uri): Song?

    fun getByAlbumId(albumId: Id): Song?

}