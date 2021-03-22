package com.alimoradi.presentation.search.di

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.search.SearchFragment


@Module(subcomponents = arrayOf(SearchFragmentSubComponent::class))
abstract class SearchFragmentInjector {

    @Binds
    @IntoMap
    @ClassKey(SearchFragment::class)
    internal abstract fun injectorFactory(builder: SearchFragmentSubComponent.Builder)
            : AndroidInjector.Factory<*>

}
