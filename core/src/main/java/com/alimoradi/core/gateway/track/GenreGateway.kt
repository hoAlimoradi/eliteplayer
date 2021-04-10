package com.alimoradi.core.gateway.track

import com.alimoradi.core.entity.track.Genre
import com.alimoradi.core.gateway.base.*

interface GenreGateway :
    BaseGateway<Genre, Id>,
    ChildHasTracks<Id>,
    HasMostPlayed,
    HasSiblings<Genre, Id>,
    HasRelatedArtists<Id>,
    HasRecentlyAddedSongs<Id>