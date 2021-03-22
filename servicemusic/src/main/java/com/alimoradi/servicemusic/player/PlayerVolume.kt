package com.alimoradi.servicemusic.player

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.alimoradi.injection.dagger.ServiceLifecycle
import com.alimoradi.injection.dagger.PerService
import com.alimoradi.core.prefs.MusicPreferencesGateway
import com.alimoradi.servicemusic.interfaces.IMaxAllowedPlayerVolume
import com.alimoradi.servicemusic.interfaces.IDuckVolume
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val VOLUME_DUCK = .2f
private const val VOLUME_LOWERED_DUCK = 0.1f

private const val VOLUME_NORMAL = 1f
private const val VOLUME_LOWERED_NORMAL = 0.4f

@PerService
internal class PlayerVolume @Inject constructor(
    @ServiceLifecycle lifecycle: Lifecycle,
    musicPreferencesUseCase: MusicPreferencesGateway

) : IMaxAllowedPlayerVolume, DefaultLifecycleObserver, CoroutineScope by MainScope() {

    override var listener: IMaxAllowedPlayerVolume.Listener? = null

    private var volume: IDuckVolume = Volume()
    private var isDucking = false

    init {
        lifecycle.addObserver(this)

        // observe to preferences
        launch {
            musicPreferencesUseCase.isMidnightMode()
                .collect { lowerAtNight ->
                    volume = if (!lowerAtNight) {
                        provideVolumeManager(false)
                    } else {
                        provideVolumeManager(isNight())
                    }

                    listener?.onMaxAllowedVolumeChanged(getMaxAllowedVolume())
                }
        }
        launch {
            // observe at interval of 15 mins to detect if is day or night when
            // settigs is on
            musicPreferencesUseCase.isMidnightMode()
                .filter { it }
                .map { delay(TimeUnit.MINUTES.toMillis(15)); it; }
                .map { isNight() }
                .collect { isNight ->
                    volume = provideVolumeManager(isNight)
                    listener?.onMaxAllowedVolumeChanged(getMaxAllowedVolume())
                }
        }
    }

    private fun isNight(): Boolean {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return hour in 0..6
    }

    override fun getMaxAllowedVolume(): Float {
        return if (isDucking) volume.duck else volume.normal
    }

    private fun provideVolumeManager(isNight: Boolean): IDuckVolume {
        return if (isNight) {
            NightVolume()
        } else {
            Volume()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cancel()
        listener = null
    }

    override fun normal(): Float {
        isDucking = false
        return volume.normal
    }

    override fun ducked(): Float {
        isDucking = true
        return volume.duck
    }
}

private class Volume : IDuckVolume {
    override val normal: Float = VOLUME_NORMAL
    override val duck: Float = VOLUME_DUCK
}

private class NightVolume : IDuckVolume {
    override val normal: Float = VOLUME_LOWERED_NORMAL
    override val duck: Float = VOLUME_LOWERED_DUCK
}
