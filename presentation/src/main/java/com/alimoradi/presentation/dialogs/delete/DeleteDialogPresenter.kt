package com.alimoradi.presentation.dialogs.delete

import com.alimoradi.core.MediaId
import com.alimoradi.core.interactor.DeleteUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteDialogPresenter @Inject constructor(
    private val deleteUseCase: DeleteUseCase
) {


    suspend fun execute(mediaId: MediaId) = withContext(Dispatchers.IO) {
        deleteUseCase(mediaId)
    }

}