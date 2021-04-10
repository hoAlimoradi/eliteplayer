package com.alimoradi.presentation.createplaylist.di

import dagger.BindsInstance
import dagger.Subcomponent
import dagger.android.AndroidInjector
import com.alimoradi.presentation.createplaylist.CreatePlaylistFragment
import com.alimoradi.presentation.dagger.PerFragment


@Subcomponent(modules = [CreatePlaylistFragmentModule::class])
@PerFragment
interface CreatePlaylistFragmentSubComponent : AndroidInjector<CreatePlaylistFragment> {

    @Subcomponent.Factory
    interface Builder : AndroidInjector.Factory<CreatePlaylistFragment> {

        override fun create(@BindsInstance instance: CreatePlaylistFragment): AndroidInjector<CreatePlaylistFragment>
    }

}