package com.alimoradi.presentation.chooser.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.chooser.PlaylistChooserActivityViewModel

@Module
abstract class PlaylistChooserActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistChooserActivityViewModel::class)
    abstract fun provideViewModel(viewModel: PlaylistChooserActivityViewModel): ViewModel

}