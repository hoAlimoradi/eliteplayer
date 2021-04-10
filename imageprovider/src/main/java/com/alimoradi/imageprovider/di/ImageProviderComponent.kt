package com.alimoradi.imageprovider.di

import dagger.Component
import com.alimoradi.imageprovider.GlideModule
import com.alimoradi.injection.CoreComponent


@Component(dependencies = [CoreComponent::class])
@PerImageProvider
interface ImageProviderComponent {

    fun inject(instance: GlideModule)

    @Component.Factory
    interface Factory {

        fun create(component: CoreComponent): ImageProviderComponent

    }

}