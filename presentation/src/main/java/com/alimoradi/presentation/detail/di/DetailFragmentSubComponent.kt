package com.alimoradi.presentation.detail.di

import dagger.BindsInstance
import dagger.Subcomponent
import dagger.android.AndroidInjector
import com.alimoradi.presentation.dagger.PerFragment
import com.alimoradi.presentation.detail.DetailFragment


@Subcomponent(
    modules = arrayOf(
        DetailFragmentModule::class
    )
)
@PerFragment
internal interface DetailFragmentSubComponent : AndroidInjector<DetailFragment> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<DetailFragment> {

        override fun create(@BindsInstance instance: DetailFragment): DetailFragmentSubComponent
    }

}
