package com.alimoradi.presentation.relatedartists.di

import dagger.BindsInstance
import dagger.Subcomponent
import dagger.android.AndroidInjector
import com.alimoradi.presentation.dagger.PerFragment
import com.alimoradi.presentation.relatedartists.RelatedArtistFragment

@Subcomponent(modules = [RelatedArtistFragmentModule::class])
@PerFragment
interface RelatedArtistFragmentSubComponent : AndroidInjector<RelatedArtistFragment> {

    @Subcomponent.Factory
    interface Builder : AndroidInjector.Factory<RelatedArtistFragment> {

        override fun create(@BindsInstance instance: RelatedArtistFragment): AndroidInjector<RelatedArtistFragment>
    }

}