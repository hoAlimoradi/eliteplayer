package com.alimoradi.presentation.search.di

import dagger.BindsInstance
import dagger.Subcomponent
import dagger.android.AndroidInjector
import com.alimoradi.presentation.dagger.PerFragment
import com.alimoradi.presentation.search.SearchFragment

@Subcomponent(modules = [SearchFragmentModule::class])
@PerFragment
interface SearchFragmentSubComponent : AndroidInjector<SearchFragment> {

    @Subcomponent.Factory
    interface Builder : AndroidInjector.Factory<SearchFragment> {

        override fun create(@BindsInstance instance: SearchFragment): AndroidInjector<SearchFragment>
    }

}