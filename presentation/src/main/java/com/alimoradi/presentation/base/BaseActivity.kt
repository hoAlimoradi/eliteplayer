package com.alimoradi.presentation.base

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dev.olog.lib.DarkDesaturatedResources
import com.alimoradi.presentation.R
import com.alimoradi.presentation.utils.setLightStatusBar
import com.alimoradi.sharedandroid.theme.isImmersiveMode
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), ThemedActivity, HasAndroidInjector {

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private var customResources: Resources? = null

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        themeAccentColor(this, theme)
        super.onCreate(savedInstanceState)
        window.setLightStatusBar()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && isImmersiveMode()) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    override fun getResources(): Resources {
        if (customResources == null){
            val res = super.getResources()
            val isDarkMode = res.getBoolean(R.bool.is_dark_mode)
            customResources = DarkDesaturatedResources(isDarkMode, res)
        }
        return customResources!!
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}
