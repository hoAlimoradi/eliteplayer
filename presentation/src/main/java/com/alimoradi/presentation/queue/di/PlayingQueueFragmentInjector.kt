package com.alimoradi.presentation.queue.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.queue.PlayingQueueFragment


@Module(subcomponents = arrayOf(PlayingQueueFragmentSubComponent::class))
abstract class PlayingQueueFragmentInjector {

    @Binds
    @IntoMap
    @ClassKey(PlayingQueueFragment::class)
    internal abstract fun injectorFactory(builder: PlayingQueueFragmentSubComponent.Builder)
            : AndroidInjector.Factory<*>

}
