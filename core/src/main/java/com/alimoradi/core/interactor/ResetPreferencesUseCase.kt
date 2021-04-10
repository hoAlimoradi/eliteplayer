package com.alimoradi.core.interactor

import com.alimoradi.core.prefs.AppPreferencesGateway
import com.alimoradi.core.prefs.BlacklistPreferences
import com.alimoradi.core.prefs.EqualizerPreferencesGateway
import com.alimoradi.core.prefs.MusicPreferencesGateway
import javax.inject.Inject

class ResetPreferencesUseCase @Inject constructor(
    private val appPrefsUseCase: AppPreferencesGateway,
    private val musicPreferencesUseCase: MusicPreferencesGateway,
    private val equalizerPrefsUseCase: EqualizerPreferencesGateway,
    private val blacklistPreferences: BlacklistPreferences
) {

    fun execute() {
        appPrefsUseCase.setDefault()
        musicPreferencesUseCase.setDefault()
        equalizerPrefsUseCase.setDefault()
        blacklistPreferences.setDefault()
    }

}