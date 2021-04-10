package com.alimoradi.presentation.tab.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.tab.TabFragment


@Module(subcomponents = arrayOf(TabFragmentSubComponent::class))
abstract class TabFragmentInjector {

    @Binds
    @IntoMap
    @ClassKey(TabFragment::class)
    internal abstract fun injectorFactory(builder: TabFragmentSubComponent.Builder)
            : AndroidInjector.Factory<*>

}
