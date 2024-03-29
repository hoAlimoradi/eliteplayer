package com.alimoradi.core.interactor

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.favorite.FavoriteType
import com.alimoradi.core.gateway.FavoriteGateway
import com.alimoradi.core.interactor.songlist.GetSongListByParamUseCase
import javax.inject.Inject

class AddToFavoriteUseCase @Inject constructor(
    private val favoriteGateway: FavoriteGateway,
    private val getSongListByParamUseCase: GetSongListByParamUseCase

) {

    suspend operator fun invoke(param: Input) {
        val mediaId = param.mediaId
        val type = param.type
        if (mediaId.isLeaf) {
            val songId = mediaId.leaf!!
            return favoriteGateway.addSingle(type, songId)
        }

        val ids = getSongListByParamUseCase(mediaId).map { it.id }
        return favoriteGateway.addGroup(type, ids)
    }

    class Input(
        val mediaId: MediaId,
        val type: FavoriteType
    )

}