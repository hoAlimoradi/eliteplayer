package com.alimoradi.eliteplayer.appwidgets

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class WidgetBindingModule {

    @ContributesAndroidInjector
    abstract fun provideWidgetColored() : WidgetColored

}