package com.alimoradi.presentation.prefs.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.alimoradi.presentation.prefs.SettingsFragment
import com.alimoradi.presentation.prefs.blacklist.BlacklistFragment
import com.alimoradi.presentation.prefs.categories.LibraryCategoriesFragment
import com.alimoradi.presentation.prefs.lastfm.LastFmCredentialsFragment

@Module
abstract class SettingsFragmentsModule {

    @ContributesAndroidInjector
    abstract fun provideLibraryCategoriesFragment() : LibraryCategoriesFragment

    @ContributesAndroidInjector
    abstract fun provideBlacklistFragment() : BlacklistFragment

    @ContributesAndroidInjector
    abstract fun provideLastFmCredentialsFragment() : LastFmCredentialsFragment

    @ContributesAndroidInjector
    abstract fun providePreferencesFragment() : SettingsFragment

}