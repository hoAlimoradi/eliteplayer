package com.alimoradi.core.gateway.podcast

import com.alimoradi.core.entity.track.Album
import com.alimoradi.core.gateway.base.*
import kotlinx.coroutines.flow.Flow

interface PodcastAlbumGateway :
    BaseGateway<Album, Id>,
    HasLastPlayed<Album>,
    HasRecentlyAdded<Album>,
    ChildHasTracks<Id>,
    HasSiblings<Album, Id> {

    fun observeArtistsAlbums(artistId: Id): Flow<List<Album>>
}