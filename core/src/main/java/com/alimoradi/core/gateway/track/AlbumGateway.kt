package com.alimoradi.core.gateway.track

import com.alimoradi.core.entity.track.Album
import com.alimoradi.core.gateway.base.*
import kotlinx.coroutines.flow.Flow

interface AlbumGateway :
    BaseGateway<Album, Id>,
    ChildHasTracks<Id>,
    HasLastPlayed<Album>,
    HasRecentlyAdded<Album>,
    HasSiblings<Album, Id> {

    fun observeArtistsAlbums(artistId: Id): Flow<List<Album>>

}