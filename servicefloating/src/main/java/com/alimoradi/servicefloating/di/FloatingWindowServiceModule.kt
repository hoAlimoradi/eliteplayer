package com.alimoradi.servicefloating.di

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import androidx.lifecycle.Lifecycle
import dagger.Binds
import dagger.Module
import dagger.Provides
import com.alimoradi.injection.dagger.ServiceContext
import com.alimoradi.injection.dagger.ServiceLifecycle
import com.alimoradi.servicefloating.FloatingWindowService

@Module
abstract class FloatingWindowServiceModule {

    @Binds
    @ServiceContext
    abstract fun provideContext(instance: FloatingWindowService): Context

    @Binds
    abstract fun provideService(instance: FloatingWindowService): Service

    @Module
    companion object {

        @Provides
        @JvmStatic
        @ServiceLifecycle
        fun provideLifecycle(instance: FloatingWindowService): Lifecycle = instance.lifecycle

        @Provides
        @JvmStatic
        internal fun provideNotificationManager(instance: FloatingWindowService): NotificationManager {
            return instance.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
    }


}