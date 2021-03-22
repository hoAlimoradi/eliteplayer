package com.alimoradi.presentation.equalizer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alimoradi.core.entity.EqualizerPreset
import com.alimoradi.core.gateway.EqualizerGateway
import com.alimoradi.core.prefs.EqualizerPreferencesGateway
import com.alimoradi.equalizer.bassboost.IBassBoost
import com.alimoradi.equalizer.equalizer.IEqualizer
import com.alimoradi.equalizer.virtualizer.IVirtualizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class EqualizerFragmentViewModel @Inject constructor(
    private val equalizer: IEqualizer,
    private val bassBoost: IBassBoost,
    private val virtualizer: IVirtualizer,
    private val equalizerPrefsUseCase: EqualizerPreferencesGateway,
    private val equalizerGateway: EqualizerGateway
) : ViewModel() {

    private val currentPresetLiveData = MutableLiveData<EqualizerPreset>()

    init {
        viewModelScope.launch {
            equalizer.observeCurrentPreset()
                .flowOn(Dispatchers.IO)
                .collect { currentPresetLiveData.value = it }
        }
    }

    fun getBandLimit() = equalizer.getBandLimit()
    fun getBandCount() = equalizer.getBandCount()
    fun setCurrentPreset(preset: EqualizerPreset) = viewModelScope.launch(Dispatchers.IO) {
        equalizer.setCurrentPreset(preset)
    }
    fun getPresets() = equalizer.getPresets()
    fun setBandLevel(band: Int, level: Float) = equalizer.setBandLevel(band, level)

    override fun onCleared() {
        viewModelScope.cancel()
    }

    fun observePreset(): LiveData<EqualizerPreset> = currentPresetLiveData

    fun isEqualizerEnabled(): Boolean = equalizerPrefsUseCase.isEqualizerEnabled()

    fun setEqualizerEnabled(enabled: Boolean) {
        equalizer.setEnabled(enabled)
        virtualizer.setEnabled(enabled)
        bassBoost.setEnabled(enabled)
        equalizerPrefsUseCase.setEqualizerEnabled(enabled)
    }

    fun getBassStrength(): Int = bassBoost.getStrength()

    fun setBassStrength(value: Int) {
        bassBoost.setStrength(value)
    }

    fun getVirtualizerStrength(): Int = virtualizer.getStrength()

    fun setVirtualizerStrength(value: Int) {
        virtualizer.setStrength(value)
    }

    fun getBandStep(): Float {
        return .1f
    }

    fun deleteCurrentPreset() = viewModelScope.launch(Dispatchers.IO) {
        val currentPreset = currentPresetLiveData.value!!
        equalizerPrefsUseCase.setCurrentPresetId(0)
        equalizerGateway.deletePreset(currentPreset)
    }

    suspend fun addPreset(title: String): Boolean = withContext(Dispatchers.IO){
        val preset = EqualizerPreset(
            id = -1,
            name = title,
            isCustom = true,
            bands = equalizer.getAllBandsCurrentLevel()
        )
        require(preset.bands.size == getBandCount()) {
            "current=${preset.bands.size}, requested=${getBandCount()}"
        }
        equalizerGateway.addPreset(preset)
        true
    }

    fun updateCurrentPresetIfCustom() = viewModelScope.launch(Dispatchers.IO) {
        equalizer.updateCurrentPresetIfCustom()
    }

}
