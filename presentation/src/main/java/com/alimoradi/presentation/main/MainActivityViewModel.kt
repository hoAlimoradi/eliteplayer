package com.alimoradi.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import dev.olog.core.dagger.ApplicationContext
import com.alimoradi.presentation.model.PresentationPreferencesGateway
import com.alimoradi.sharedandroid.Permissions
import javax.inject.Inject

internal class MainActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val presentationPrefs: PresentationPreferencesGateway
) : ViewModel() {


    fun isFirstAccess(): Boolean {
        val canReadStorage = Permissions.canReadStorage(context)
        val isFirstAccess = presentationPrefs.isFirstAccess()
        return !canReadStorage || isFirstAccess
    }

}