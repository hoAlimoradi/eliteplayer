package com.alimoradi.presentation.main.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import com.alimoradi.injection.dagger.ActivityContext
import com.alimoradi.media.MediaProvider
import com.alimoradi.presentation.dagger.ActivityLifecycle
import com.alimoradi.presentation.dagger.PerActivity
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.main.MainActivity
import com.alimoradi.presentation.main.MainActivityViewModel
import com.alimoradi.presentation.navigator.Navigator
import com.alimoradi.presentation.navigator.NavigatorImpl
import com.alimoradi.presentation.pro.BillingMock
import com.alimoradi.presentation.pro.IBilling

@Module
abstract class MainActivityModule {

    @Binds
    @ActivityContext
    internal abstract fun provideContext(instance: MainActivity): Context

    @Binds
    internal abstract fun provideFragmentActivity(instance: MainActivity): FragmentActivity

    @Binds
    internal abstract fun provideMusicGlue(instance: MainActivity): MediaProvider

    @Binds
    @PerActivity
    abstract fun provideNavigator(navigatorImpl: NavigatorImpl): Navigator

    @Binds
    @PerActivity
    internal abstract fun provideBilling(impl: BillingMock): IBilling

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun proviewViewModel(impl: MainActivityViewModel): ViewModel

    @Module
    companion object {

        @Provides
        @JvmStatic
        @ActivityLifecycle
        internal fun provideLifecycle(instance: MainActivity): Lifecycle = instance.lifecycle

    }

}