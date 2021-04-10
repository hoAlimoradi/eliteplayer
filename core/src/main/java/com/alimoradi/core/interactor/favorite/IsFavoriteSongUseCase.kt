package com.alimoradi.core.interactor.favorite

import com.alimoradi.core.entity.favorite.FavoriteType
import com.alimoradi.core.gateway.FavoriteGateway
import javax.inject.Inject

class IsFavoriteSongUseCase @Inject constructor(
    private val gateway: FavoriteGateway
) {

    suspend operator fun invoke(param: Input): Boolean {
        if (param.type == FavoriteType.TRACK) {
            return gateway.isFavorite(param.songId)
        }
        return gateway.isFavoritePodcast(param.songId)
    }

    class Input(
        val songId: Long,
        val type: FavoriteType
    )
}
