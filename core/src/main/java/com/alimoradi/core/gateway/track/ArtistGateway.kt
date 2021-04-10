package com.alimoradi.core.gateway.track

import com.alimoradi.core.entity.track.Artist
import com.alimoradi.core.gateway.base.*

interface ArtistGateway :
    BaseGateway<Artist, Id>,
    ChildHasTracks<Id>,
    HasRecentlyAdded<Artist>,
    HasLastPlayed<Artist>