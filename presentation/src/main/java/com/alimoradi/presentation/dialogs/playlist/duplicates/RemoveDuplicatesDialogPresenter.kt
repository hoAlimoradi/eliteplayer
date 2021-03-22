package com.alimoradi.presentation.dialogs.playlist.duplicates

import com.alimoradi.core.MediaId
import com.alimoradi.core.interactor.playlist.RemoveDuplicatesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoveDuplicatesDialogPresenter @Inject constructor(
    private val useCase: RemoveDuplicatesUseCase
) {

    suspend fun execute(mediaId: MediaId) = withContext(Dispatchers.IO){
        useCase(mediaId)
    }

}