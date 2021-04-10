package com.alimoradi.presentation.player.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.player.PlayerFragment
import com.alimoradi.presentation.player.PlayerFragmentViewModel
import com.alimoradi.presentation.player.volume.PlayerVolumeFragment

@Module
abstract class PlayerFragmentModule {

    @ContributesAndroidInjector
    abstract fun provideFragment(): PlayerFragment

    @ContributesAndroidInjector
    abstract fun provideVolumeFragment(): PlayerVolumeFragment

    @Binds
    @IntoMap
    @ViewModelKey(PlayerFragmentViewModel::class)
    internal abstract fun provideViewModel(viewModel: PlayerFragmentViewModel): ViewModel

}