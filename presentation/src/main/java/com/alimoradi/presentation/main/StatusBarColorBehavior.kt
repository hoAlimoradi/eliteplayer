package com.alimoradi.presentation.main

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.alimoradi.presentation.interfaces.CanChangeStatusBarColor
import com.alimoradi.presentation.interfaces.HasSlidingPanel
import com.alimoradi.presentation.utils.removeLightStatusBar
import com.alimoradi.presentation.utils.setLightStatusBar
import dev.olog.scrollhelper.MultiListenerBottomSheetBehavior
import com.alimoradi.sharedandroid.theme.hasPlayerAppearance
import com.alimoradi.sharedandroid.utils.isMarshmallow
import com.alimoradi.shared.lazyFast
import java.lang.ref.WeakReference
import javax.inject.Inject

class StatusBarColorBehavior @Inject constructor(
    fragmentActivity: FragmentActivity
) : DefaultLifecycleObserver, FragmentManager.OnBackStackChangedListener {

    private val activityRef = WeakReference(fragmentActivity)

    private val slidingPanel: MultiListenerBottomSheetBehavior<*>? by lazyFast {
        val activity = activityRef.get() ?: return@lazyFast null
        (activity as HasSlidingPanel).getSlidingPanel()
    }

    init {
        fragmentActivity.lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        val activity = activityRef.get() ?: return

        if (!isMarshmallow()){
            return
        }

        slidingPanel?.addPanelSlideListener(slidingPanelListener)
        activity.supportFragmentManager.addOnBackStackChangedListener(this)
    }

    override fun onPause(owner: LifecycleOwner) {
        val activity = activityRef.get() ?: return

        if (!isMarshmallow()){
            return
        }

        slidingPanel?.removePanelSlideListener(slidingPanelListener)
        activity.supportFragmentManager.removeOnBackStackChangedListener(this)
    }

    override fun onBackStackChanged() {
        val activity = activityRef.get() ?: return

        if (!isMarshmallow()){
            return
        }

        val fragment = searchFragmentWithLightStatusBar(activity)
        if (fragment == null){
            activity.window.setLightStatusBar()
        } else {
            if (slidingPanel?.state == BottomSheetBehavior.STATE_EXPANDED){
                activity.window.setLightStatusBar()
            } else {
                fragment.adjustStatusBarColor()
            }
        }
    }

    private fun searchFragmentWithLightStatusBar(activity: FragmentActivity): CanChangeStatusBarColor? {
        val fm = activity.supportFragmentManager
        val backStackEntryCount = fm.backStackEntryCount - 1
        if (backStackEntryCount > -1) {
            val entry = fm.getBackStackEntryAt(backStackEntryCount)
            val fragment = fm.findFragmentByTag(entry.name)
            if (fragment is CanChangeStatusBarColor) {
                return fragment
            }
        }
        return null
    }

    private val slidingPanelListener = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        @SuppressLint("SwitchIntDef")
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            val activity = activityRef.get() ?: return

            when (newState) {
                BottomSheetBehavior.STATE_EXPANDED -> {
                    val playerApperance = (activity.hasPlayerAppearance())
                    if (playerApperance.isFullscreen() || playerApperance.isBigImage()) {
                        activity.window.removeLightStatusBar()
                    } else {
                        activity.window.setLightStatusBar()
                    }
                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    searchFragmentWithLightStatusBar(activity)?.adjustStatusBarColor() ?: activity.window.setLightStatusBar()
                }
            }
        }
    }

}