package com.alimoradi.presentation.dialogs.playlist.rename

import com.alimoradi.core.MediaId
import com.alimoradi.core.interactor.playlist.RenameUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RenameDialogPresenter @Inject constructor(
    private val renameUseCase: RenameUseCase
) {


    suspend fun execute(mediaId: MediaId, newTitle: String) = withContext(Dispatchers.IO){
        renameUseCase(mediaId, newTitle)
    }

}