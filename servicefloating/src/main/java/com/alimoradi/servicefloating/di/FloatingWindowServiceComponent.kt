package com.alimoradi.servicefloating.di

import dagger.BindsInstance
import dagger.Component
import com.alimoradi.injection.CoreComponent
import com.alimoradi.injection.dagger.PerService
import com.alimoradi.servicefloating.FloatingWindowService

fun FloatingWindowService.inject(){
    DaggerFloatingWindowServiceComponent.factory()
            .create(this, CoreComponent.coreComponent(application))
            .inject(this)
}

@Component(modules = arrayOf(
        FloatingWindowServiceModule::class
), dependencies = [CoreComponent::class])
@PerService
interface FloatingWindowServiceComponent {

    fun inject(instance: FloatingWindowService)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance instance: FloatingWindowService, component: CoreComponent): FloatingWindowServiceComponent

    }

}