package com.alimoradi.core.interactor.favorite

import com.alimoradi.core.entity.favorite.FavoriteEnum
import com.alimoradi.core.gateway.FavoriteGateway
import com.alimoradi.core.interactor.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoriteAnimationUseCase @Inject constructor(
    private val gateway: FavoriteGateway

) : FlowUseCase<FavoriteEnum>() {

    override fun buildUseCase(): Flow<FavoriteEnum> {
        return gateway.observeToggleFavorite()
    }
}