package com.alimoradi.core.interactor.favorite

import com.alimoradi.core.entity.favorite.FavoriteStateEntity
import com.alimoradi.core.gateway.FavoriteGateway
import javax.inject.Inject

class UpdateFavoriteStateUseCase @Inject constructor(
    private val favoriteGateway: FavoriteGateway

) {

    suspend operator fun invoke(param: FavoriteStateEntity) {
        return favoriteGateway.updateFavoriteState(param)
    }
}