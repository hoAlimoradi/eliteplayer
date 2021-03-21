package com.alimoradi.presentation.tab.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import com.alimoradi.presentation.dagger.PerFragment
import com.alimoradi.presentation.tab.TabFragment

@Subcomponent(modules = [TabFragmentModule::class])
@PerFragment
interface TabFragmentSubComponent : AndroidInjector<TabFragment> {

    @Subcomponent.Factory
    interface Builder : AndroidInjector.Factory<TabFragment>

}