package com.alimoradi.eliteplayer.theme

import android.content.Context
import android.content.SharedPreferences
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.presentation.R
import com.alimoradi.sharedandroid.theme.QuickAction
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import javax.inject.Inject

internal class QuickActionListener @Inject constructor(
    @ApplicationContext context: Context,
    prefs: SharedPreferences
) : BaseThemeUpdater<QuickAction>(
    context,
    prefs,
    context.getString(R.string.prefs_quick_action_key)
) {

    val quickActionPublisher by lazy { ConflatedBroadcastChannel(getValue()) }
    fun quickAction() = quickActionPublisher.value

    override fun onPrefsChanged() {

        val quickActon = getValue()
        quickActionPublisher.offer(quickActon)
    }

    override fun getValue(): QuickAction {
        val value =
            prefs.getString(key, context.getString(R.string.prefs_quick_action_entry_value_hide))


        return when (value) {
            context.getString(R.string.prefs_quick_action_entry_value_hide) -> QuickAction.NONE
            context.getString(R.string.prefs_quick_action_entry_value_play) -> QuickAction.PLAY
            else -> QuickAction.SHUFFLE
        }
    }
}

