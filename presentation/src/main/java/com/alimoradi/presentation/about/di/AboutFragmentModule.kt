package com.alimoradi.presentation.about.di

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.alimoradi.presentation.about.AboutFragment
import com.alimoradi.presentation.navigator.NavigatorAbout
import com.alimoradi.presentation.navigator.NavigatorAboutImpl
import com.alimoradi.presentation.translations.TranslationsFragment

@Module
abstract class AboutFragmentModule {

    @ContributesAndroidInjector
    abstract fun provideAboutFragment(): AboutFragment

    @ContributesAndroidInjector
    abstract fun provideTranslationFragment(): TranslationsFragment

    @Binds
    abstract fun provideNavigatorAbout(navigatorImpl: NavigatorAboutImpl): NavigatorAbout

}