package com.alimoradi.eliteplayer.app

import androidx.preference.PreferenceManager
import com.alimoradi.analytics.TrackerFacade
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import com.alimoradi.appshortcuts.AppShortcuts
import com.alimoradi.core.interactor.SleepTimerUseCase
import com.alimoradi.injection.CoreComponent
import com.alimoradi.eliteplayer.BuildConfig
import com.alimoradi.eliteplayer.R
import com.alimoradi.eliteplayer.tracker.ActivityAndFragmentsTracker
import com.alimoradi.sharedandroid.extensions.configuration
import io.alterac.blurkit.BlurKit
import javax.inject.Inject

class App : ThemedApp(), HasAndroidInjector {

    private lateinit var appShortcuts: AppShortcuts

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>


    @Inject
    lateinit var sleepTimerUseCase: SleepTimerUseCase

    @Inject
    lateinit var trackerFacade: TrackerFacade

    override fun onCreate() {
        super.onCreate()
        inject()
        initializeComponents()
        initializeConstants()
        resetSleepTimer()

        registerActivityLifecycleCallbacks(CustomTabsActivityLifecycleCallback())
        registerActivityLifecycleCallbacks(ActivityAndFragmentsTracker(trackerFacade))
    }

    private fun initializeComponents() {
        appShortcuts = AppShortcuts.instance(this)

        BlurKit.init(this)
        if (BuildConfig.DEBUG) {
//            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initializeConstants() {
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false)
    }

    private fun resetSleepTimer() {
        sleepTimerUseCase.reset()
    }

    private fun inject() {
        DaggerAppComponent.factory()
            .create(CoreComponent.coreComponent(this))
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
