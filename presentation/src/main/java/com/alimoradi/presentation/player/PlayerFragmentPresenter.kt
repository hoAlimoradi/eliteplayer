package com.alimoradi.presentation.player

import android.content.Context
import dev.olog.core.dagger.ApplicationContext
import dev.olog.core.prefs.AppPreferencesGateway
import com.alimoradi.presentation.model.PresentationPreferencesGateway
import com.alimoradi.presentation.pro.IBilling
import com.alimoradi.sharedandroid.theme.hasPlayerAppearance
import com.alimoradi.sharedwidgets.adaptive.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

internal class PlayerFragmentPresenter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val billing: IBilling,
    private val prefsGateway: AppPreferencesGateway,
    private val presentationPrefs: PresentationPreferencesGateway
) {

    private val processorPublisher = ConflatedBroadcastChannel<ProcessorColors>()
    private val palettePublisher = ConflatedBroadcastChannel<PaletteColors>()

    fun observePlayerControlsVisibility(): Flow<Boolean> {

        return billing.observeBillingsState().map { it.isPremiumEnabled() }
            .combine(presentationPrefs.observePlayerControlsVisibility())
            { premium, show -> premium && show }
    }

    // allow adaptive color on flat appearance
    fun observeProcessorColors(): Flow<ProcessorColors> {

        return processorPublisher.asFlow()
            .map {
                val hasPlayerAppearance = context.hasPlayerAppearance()
                if (presentationPrefs.isAdaptiveColorEnabled() || hasPlayerAppearance.isFlat()) {
                    it
                } else {
                    InvalidProcessColors
                }
            }
            .filter { it is ValidProcessorColors }
            .flowOn(Dispatchers.Default)
    }

    // allow adaptive color on flat appearance
    fun observePaletteColors(): Flow<PaletteColors> {

        return palettePublisher
            .asFlow()
            .map {
                val hasPlayerAppearance = context.hasPlayerAppearance()
                if (presentationPrefs.isAdaptiveColorEnabled() || hasPlayerAppearance.isFlat() || hasPlayerAppearance.isSpotify()) {
                    it
                } else {
                    InvalidPaletteColors
                }
            }
            .filter { it is ValidPaletteColors }
            .flowOn(Dispatchers.Default)
    }

    fun updateProcessorColors(palette: ProcessorColors) {
        processorPublisher.offer(palette)
    }

    fun updatePaletteColors(palette: PaletteColors) {
        palettePublisher.offer(palette)
    }


}