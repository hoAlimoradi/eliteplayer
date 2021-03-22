package com.alimoradi.core.gateway.track

import com.alimoradi.core.entity.track.Folder
import com.alimoradi.core.gateway.base.*

interface FolderGateway :
    BaseGateway<Folder, Path>,
    ChildHasTracks<Path>,
    HasMostPlayed,
    HasSiblings<Folder, Path>,
    HasRelatedArtists<Path>,
    HasRecentlyAddedSongs<Path> {

    fun getAllBlacklistedIncluded(): List<Folder>

    /**
     * Hashcode = path.tohashCode()
     */
    fun getByHashCode(hashCode: Int): Folder?

}