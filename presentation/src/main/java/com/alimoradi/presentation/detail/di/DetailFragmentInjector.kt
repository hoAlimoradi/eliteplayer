package com.alimoradi.presentation.detail.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.detail.DetailFragment

@Module(subcomponents = [DetailFragmentSubComponent::class])
abstract class DetailFragmentInjector {

    @Binds
    @IntoMap
    @ClassKey(DetailFragment::class)
    internal abstract fun injectorFactory(builder: DetailFragmentSubComponent.Factory)
            : AndroidInjector.Factory<*>

}
