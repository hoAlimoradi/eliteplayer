package com.alimoradi.presentation.search.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.search.SearchFragmentViewModel

@Module
abstract class SearchFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchFragmentViewModel::class)
    abstract fun provideViewModel(factory: SearchFragmentViewModel): ViewModel

}