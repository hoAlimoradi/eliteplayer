package com.alimoradi.core.gateway.podcast

import com.alimoradi.core.entity.track.Artist
import com.alimoradi.core.gateway.base.*

interface PodcastArtistGateway :
    BaseGateway<Artist, Id>,
    HasLastPlayed<Artist>,
    HasRecentlyAdded<Artist>,
    ChildHasTracks<Id>,
    HasSiblings<Artist, Id>