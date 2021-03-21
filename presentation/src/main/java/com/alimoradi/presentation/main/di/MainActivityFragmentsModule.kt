package com.alimoradi.presentation.main.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.alimoradi.presentation.library.LibraryFragment
import com.alimoradi.presentation.offlinelyrics.OfflineLyricsFragment
import com.alimoradi.presentation.playermini.MiniPlayerFragment
import com.alimoradi.presentation.sleeptimer.SleepTimerPickerDialog

@Module
abstract class MainActivityFragmentsModule {

    @ContributesAndroidInjector
    internal abstract fun provideMiniPlayer(): MiniPlayerFragment

    @ContributesAndroidInjector
    internal abstract fun provideSleepTimerDialog(): SleepTimerPickerDialog

    @ContributesAndroidInjector
    internal abstract fun provideOfflineLyricsFragment(): OfflineLyricsFragment

    @ContributesAndroidInjector
    internal abstract fun provideCategoriesFragment(): LibraryFragment
}