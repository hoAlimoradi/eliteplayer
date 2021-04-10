package com.alimoradi.presentation.recentlyadded.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.recentlyadded.RecentlyAddedFragment

@Module(subcomponents = arrayOf(RecentlyAddedFragmentSubComponent::class))
abstract class RecentlyAddedFragmentInjector {

    @Binds
    @IntoMap
    @ClassKey(RecentlyAddedFragment::class)
    internal abstract fun injectorFactory(builder: RecentlyAddedFragmentSubComponent.Builder)
            : AndroidInjector.Factory<*>

}
