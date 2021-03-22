package com.alimoradi.presentation.dialogs.favorite

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.favorite.FavoriteType
import com.alimoradi.core.interactor.AddToFavoriteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddFavoriteDialogPresenter @Inject constructor(
    private val addToFavoriteUseCase: AddToFavoriteUseCase
) {

    suspend fun execute(mediaId: MediaId) = withContext(Dispatchers.IO) {
        val type = if (mediaId.isAnyPodcast) FavoriteType.PODCAST else FavoriteType.TRACK
        addToFavoriteUseCase(AddToFavoriteUseCase.Input(mediaId, type))
    }

}