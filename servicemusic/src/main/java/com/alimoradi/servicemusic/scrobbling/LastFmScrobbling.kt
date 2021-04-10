package com.alimoradi.servicemusic.scrobbling

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.alimoradi.core.interactor.ObserveLastFmUserCredentials
import com.alimoradi.servicemusic.interfaces.IPlayerLifecycle
import com.alimoradi.servicemusic.model.MetadataEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class LastFmScrobbling @Inject constructor(
    observeLastFmUserCredentials: ObserveLastFmUserCredentials,
    playerLifecycle: IPlayerLifecycle,
    private val lastFmService: LastFmService

) : DefaultLifecycleObserver,
    IPlayerLifecycle.Listener,
    CoroutineScope by MainScope() {

    init {
        playerLifecycle.addListener(this)

        launch {
            observeLastFmUserCredentials()
                .filter { it.username.isNotBlank() }
                .collect { lastFmService::tryAuthenticate }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cancel()
        lastFmService.dispose()
    }

    override fun onMetadataChanged(metadata: MetadataEntity) {
        lastFmService.scrobble(metadata.entity)
    }

}