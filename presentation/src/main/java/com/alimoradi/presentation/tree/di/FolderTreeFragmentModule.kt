package com.alimoradi.presentation.tree.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.tree.FolderTreeFragment
import com.alimoradi.presentation.tree.FolderTreeFragmentViewModel

@Module
abstract class FolderTreeFragmentModule {

    @ContributesAndroidInjector
    internal abstract fun provideFolderTreeFragment(): FolderTreeFragment

    @Binds
    @IntoMap
    @ViewModelKey(FolderTreeFragmentViewModel::class)
    internal abstract fun provideViewModel(viewModel: FolderTreeFragmentViewModel): ViewModel

}