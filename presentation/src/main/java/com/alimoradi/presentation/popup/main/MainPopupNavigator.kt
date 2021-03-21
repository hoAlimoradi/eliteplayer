package com.alimoradi.presentation.popup.main

import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.alimoradi.presentation.R
import com.alimoradi.presentation.about.AboutFragment
import com.alimoradi.presentation.equalizer.EqualizerFragment
import com.alimoradi.presentation.navigator.superCerealTransition
import com.alimoradi.presentation.prefs.SettingsFragmentWrapper
import com.alimoradi.presentation.pro.IBilling
import com.alimoradi.presentation.sleeptimer.SleepTimerPickerDialogBuilder
import com.alimoradi.sharedandroid.extensions.toast
import java.lang.ref.WeakReference
import javax.inject.Inject

class MainPopupNavigator @Inject constructor(
    activity: FragmentActivity,
    billing: IBilling
) {

    private val activityRef = WeakReference(activity)
    private val billingRef = WeakReference(billing)

    fun toAboutActivity() {
        val activity = activityRef.get() ?: return

        superCerealTransition(activity, AboutFragment(), AboutFragment.TAG)
    }

    fun toEqualizer() {
        val activity = activityRef.get() ?: return
        val billing = billingRef.get() ?: return

        val useCustomEqualizer = PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)
                .getBoolean(activity.getString(R.string.prefs_used_equalizer_key), true)

        if (billing.getBillingsState().isPremiumEnabled() && useCustomEqualizer) {
            toBuiltInEqualizer()
        } else {
            searchForEqualizer()
        }
    }

    private fun toBuiltInEqualizer() {
        val activity = activityRef.get() ?: return

        val instance = EqualizerFragment.newInstance()
        instance.show(activity.supportFragmentManager, EqualizerFragment.TAG)
    }

    private fun searchForEqualizer() {
        val activity = activityRef.get() ?: return

        val intent = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(intent)
        } else {
            activity.toast(R.string.equalizer_not_found)
        }
    }

    fun toSettingsActivity() {
        val activity = activityRef.get() ?: return

        superCerealTransition(activity, SettingsFragmentWrapper(), SettingsFragmentWrapper.TAG)
    }

    fun toSleepTimer() {
        val activity = activityRef.get() ?: return

        SleepTimerPickerDialogBuilder(activity, activity.supportFragmentManager).show()
    }

}