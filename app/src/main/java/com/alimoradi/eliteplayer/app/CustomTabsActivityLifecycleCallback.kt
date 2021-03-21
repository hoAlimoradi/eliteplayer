package com.alimoradi.eliteplayer.app

import android.app.Activity
import android.app.Application
import android.os.Bundle
import saschpe.android.customtabs.CustomTabsHelper

class CustomTabsActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

    private var tabHelper : CustomTabsHelper? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        tabHelper = CustomTabsHelper()
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        try {
            tabHelper?.bindCustomTabsService(activity)
        } catch (ex: Throwable){
            ex.printStackTrace()
        }

    }

    override fun onActivityPaused(activity: Activity) {
        try {
            tabHelper?.unbindCustomTabsService(activity)
        } catch (ex: Throwable){
            ex.printStackTrace()
        }

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }
}