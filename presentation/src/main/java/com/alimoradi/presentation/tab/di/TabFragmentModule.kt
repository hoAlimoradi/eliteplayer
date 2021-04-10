package com.alimoradi.presentation.tab.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.tab.TabFragmentViewModel

@Module
internal abstract class TabFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(TabFragmentViewModel::class)
    abstract fun provideViewModel(viewModel: TabFragmentViewModel): ViewModel

}