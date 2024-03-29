package com.alimoradi.eliteplayer.theme

import android.content.Context
import android.content.SharedPreferences
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.eliteplayer.theme.observer.ActivityLifecycleCallbacks
import com.alimoradi.eliteplayer.theme.observer.CurrentActivityObserver
import com.alimoradi.eliteplayer.R
import com.alimoradi.shared.mutableLazy
import com.alimoradi.sharedandroid.theme.PlayerAppearance
import javax.inject.Inject

internal class PlayerAppearanceListener @Inject constructor(
    @ApplicationContext context: Context,
    prefs: SharedPreferences
) : BaseThemeUpdater<PlayerAppearance>(
    context,
    prefs,
    context.getString(R.string.prefs_appearance_key)
), ActivityLifecycleCallbacks by CurrentActivityObserver(context) {

    var playerAppearance by mutableLazy { getValue() }
        private set

    override fun onPrefsChanged() {
        playerAppearance = getValue()
        currentActivity?.recreate()
    }

    override fun getValue(): PlayerAppearance {
        val value =
            prefs.getString(key, context.getString(R.string.prefs_appearance_entry_value_default))

        return when (value) {
            context.getString(R.string.prefs_appearance_entry_value_default) -> PlayerAppearance.DEFAULT
            context.getString(R.string.prefs_appearance_entry_value_flat) -> PlayerAppearance.FLAT
            context.getString(R.string.prefs_appearance_entry_value_spotify) -> PlayerAppearance.SPOTIFY
            context.getString(R.string.prefs_appearance_entry_value_fullscreen) -> PlayerAppearance.FULLSCREEN
            context.getString(R.string.prefs_appearance_entry_value_big_image) -> PlayerAppearance.BIG_IMAGE
            context.getString(R.string.prefs_appearance_entry_value_clean) -> PlayerAppearance.CLEAN
            context.getString(R.string.prefs_appearance_entry_value_mini) -> PlayerAppearance.MINI
            else -> throw IllegalStateException("invalid theme=$value")
        }
    }

}
