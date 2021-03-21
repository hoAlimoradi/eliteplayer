package com.alimoradi.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import com.alimoradi.analytics.tracker.FirebaseTracker
import dev.olog.core.dagger.ApplicationContext
import javax.inject.Singleton

@Module
class AnalyticsModule {

    @Provides
    internal fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
        return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @Singleton
    internal fun provideTrackerFacade(impl: FirebaseTracker): TrackerFacade = impl

}