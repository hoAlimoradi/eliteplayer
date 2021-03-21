package com.alimoradi.servicemusic.di

import dagger.BindsInstance
import dagger.Component
import com.alimoradi.injection.CoreComponent
import com.alimoradi.injection.dagger.PerService
import com.alimoradi.servicemusic.MusicService
import com.alimoradi.servicemusic.notification.NotificationModule

internal fun MusicService.inject() {
    val coreComponent = CoreComponent.coreComponent(application)
    DaggerMusicServiceComponent.factory().create(this, coreComponent)
            .inject(this)
}

@Component(modules = [
    MusicServiceModule::class,
    NotificationModule::class
], dependencies = [CoreComponent::class])
@PerService
interface MusicServiceComponent {

    fun inject(instance: MusicService)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance instance: MusicService, component: CoreComponent): MusicServiceComponent

    }
}