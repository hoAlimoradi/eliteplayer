package com.alimoradi.core.gateway.podcast

import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.base.BaseGateway
import com.alimoradi.core.gateway.base.Id

interface PodcastGateway :
    BaseGateway<Song, Id> {

    suspend fun deleteSingle(id: Id)
    suspend fun deleteGroup(podcastList: List<Song>)

    fun getCurrentPosition(podcastId: Long, duration: Long): Long
    fun saveCurrentPosition(podcastId: Long, position: Long)

    fun getByAlbumId(albumId: Id): Song?
}