package com.alimoradi.presentation.dialogs.playlist.clear

import com.alimoradi.core.MediaId
import com.alimoradi.core.interactor.playlist.ClearPlaylistUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClearPlaylistDialogPresenter @Inject constructor(
    private val useCase: ClearPlaylistUseCase

) {

    suspend fun execute(mediaId: MediaId) = withContext(Dispatchers.IO) {
        useCase(mediaId)
    }

}