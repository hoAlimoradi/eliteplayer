package com.alimoradi.eliteplayer.theme

import android.content.Context
import android.content.SharedPreferences
import dev.olog.core.dagger.ApplicationContext
import com.alimoradi.eliteplayer.theme.observer.ActivityLifecycleCallbacks
import com.alimoradi.eliteplayer.theme.observer.CurrentActivityObserver
import com.alimoradi.presentation.R
import com.alimoradi.presentation.widgets.StatusBarView
import com.alimoradi.shared.mutableLazy
import javax.inject.Inject

internal class ImmersiveModeListener @Inject constructor(
    @ApplicationContext context: Context,
    prefs: SharedPreferences
) : BaseThemeUpdater<Boolean>(context, prefs, context.getString(R.string.prefs_immersive_key)),
    ActivityLifecycleCallbacks by CurrentActivityObserver(context) {

    var isImmersive by mutableLazy { getValue() }
        private set

    override fun onPrefsChanged() {
        StatusBarView.viewHeight = -1
        isImmersive = getValue()
        currentActivity?.recreate()
    }

    override fun getValue(): Boolean {
        return prefs.getBoolean(key, false)
    }

}