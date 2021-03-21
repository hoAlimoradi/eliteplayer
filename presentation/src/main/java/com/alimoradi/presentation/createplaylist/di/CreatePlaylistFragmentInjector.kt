package com.alimoradi.presentation.createplaylist.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.createplaylist.CreatePlaylistFragment

@Module(subcomponents = arrayOf(CreatePlaylistFragmentSubComponent::class))
abstract class CreatePlaylistFragmentInjector {

    @Binds
    @IntoMap
    @ClassKey(CreatePlaylistFragment::class)
    internal abstract fun injectorFactory(builder: CreatePlaylistFragmentSubComponent.Builder)
            : AndroidInjector.Factory<*>

}
