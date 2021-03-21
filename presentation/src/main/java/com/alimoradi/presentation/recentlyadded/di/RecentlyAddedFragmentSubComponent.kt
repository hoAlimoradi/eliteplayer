package com.alimoradi.presentation.recentlyadded.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import com.alimoradi.presentation.dagger.PerFragment
import com.alimoradi.presentation.recentlyadded.RecentlyAddedFragment

@Subcomponent(
    modules = [RecentlyAddedFragmentModule::class]
)
@PerFragment
interface RecentlyAddedFragmentSubComponent : AndroidInjector<RecentlyAddedFragment> {

    @Subcomponent.Factory
    interface Builder : AndroidInjector.Factory<RecentlyAddedFragment>

}