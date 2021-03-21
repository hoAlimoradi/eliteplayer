package com.alimoradi.servicemusic.di

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import androidx.lifecycle.Lifecycle
import androidx.media.session.MediaButtonReceiver
import dagger.Binds
import dagger.Module
import dagger.Provides
import com.alimoradi.injection.dagger.PerService
import com.alimoradi.injection.dagger.ServiceContext
import com.alimoradi.injection.dagger.ServiceLifecycle
import com.alimoradi.servicemusic.MusicService
import com.alimoradi.servicemusic.interfaces.*
import com.alimoradi.servicemusic.model.PlayerMediaEntity
import com.alimoradi.servicemusic.player.PlayerImpl
import com.alimoradi.servicemusic.player.PlayerVolume
import com.alimoradi.servicemusic.player.crossfade.CrossFadePlayerSwitcher
import com.alimoradi.servicemusic.queue.QueueManager

@Module
abstract class MusicServiceModule {

    @Binds
    @ServiceContext
    internal abstract fun provideContext(instance: MusicService): Context

    @Binds
    internal abstract fun provideService(instance: MusicService): Service

    @Binds
    @PerService
    internal abstract fun provideServiceLifecycle(instance: MusicService): IServiceLifecycleController

    @Binds
    @PerService
    internal abstract fun provideQueue(queue: QueueManager): IQueue

    @Binds
    @PerService
    internal abstract fun providePlayer(player: PlayerImpl): IPlayer

    @Binds
    @PerService
    internal abstract fun providePlayerLifecycle(player: IPlayer): IPlayerLifecycle

    @Binds
    @PerService
    internal abstract fun providePlayerVolume(volume: PlayerVolume): IMaxAllowedPlayerVolume

    @Binds
    @PerService
    internal abstract fun providePlayerImpl(impl: CrossFadePlayerSwitcher): IPlayerDelegate<PlayerMediaEntity>

    @Module
    companion object {
        @Provides
        @JvmStatic
        @ServiceLifecycle
        internal fun provideLifecycle(instance: MusicService): Lifecycle = instance.lifecycle

        @Provides
        @JvmStatic
        @PerService
        internal fun provideMediaSession(instance: MusicService): MediaSessionCompat {
            return MediaSessionCompat(
                instance,
                MusicService.TAG,
                ComponentName(instance, MediaButtonReceiver::class.java),
                null
            )
        }
    }

}