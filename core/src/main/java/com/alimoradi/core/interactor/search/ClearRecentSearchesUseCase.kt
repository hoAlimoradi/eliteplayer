package com.alimoradi.core.interactor.search

import com.alimoradi.core.gateway.RecentSearchesGateway
import javax.inject.Inject

class ClearRecentSearchesUseCase @Inject constructor(
    private val recentSearchesGateway: RecentSearchesGateway

) {

    suspend operator fun invoke() {
        recentSearchesGateway.deleteAll()
    }
}