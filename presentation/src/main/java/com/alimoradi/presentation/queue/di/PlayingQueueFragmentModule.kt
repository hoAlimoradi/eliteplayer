package com.alimoradi.presentation.queue.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.queue.PlayingQueueFragmentViewModel

@Module
abstract class PlayingQueueFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlayingQueueFragmentViewModel::class)
    abstract fun provideViewModel(viewModel: PlayingQueueFragmentViewModel): ViewModel

}