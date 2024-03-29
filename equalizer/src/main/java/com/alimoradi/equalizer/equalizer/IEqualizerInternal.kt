package com.alimoradi.equalizer.equalizer

import com.alimoradi.core.entity.EqualizerBand
import com.alimoradi.core.entity.EqualizerPreset
import kotlinx.coroutines.flow.Flow

internal interface IEqualizerInternal {

    fun onAudioSessionIdChanged(audioSessionId: Int)
    fun onDestroy()
    fun setEnabled(enabled: Boolean)

    fun getPresets(): List<EqualizerPreset>
    fun observeCurrentPreset(): Flow<EqualizerPreset>
    fun getCurrentPreset(): EqualizerPreset
    suspend fun setCurrentPreset(preset: EqualizerPreset)
    suspend fun updateCurrentPresetIfCustom()

    fun getBandCount(): Int
    fun getBandLevel(band: Int): Float
    fun getAllBandsCurrentLevel(): List<EqualizerBand>
    fun setBandLevel(band: Int, level: Float)
    fun getBandLimit(): Float

}