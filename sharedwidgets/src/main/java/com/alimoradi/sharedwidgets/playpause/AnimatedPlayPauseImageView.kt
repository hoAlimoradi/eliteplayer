package com.alimoradi.sharedwidgets.playpause

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import com.alimoradi.sharedandroid.extensions.isDarkMode
import com.alimoradi.shared.lazyFast
import com.alimoradi.sharedandroid.theme.hasPlayerAppearance
import com.alimoradi.sharedwidgets.ColorDelegateImpl
import com.alimoradi.sharedwidgets.IColorDelegate

class AnimatedPlayPauseImageView(
        context: Context,
        attrs: AttributeSet

) : AppCompatImageButton(context, attrs),
    IPlayPauseBehavior,
    IColorDelegate by ColorDelegateImpl {

    private val playerAppearance by lazyFast { context.hasPlayerAppearance() }
    private val behavior = PlayPauseBehaviorImpl(this)

    private val isDarkMode by lazyFast { context.isDarkMode() }

    fun setDefaultColor() {
        val defaultColor = getDefaultColor(context, playerAppearance, isDarkMode)
        setColorFilter(defaultColor)
    }

    fun useLightImage() {
        setColorFilter(lightColor())
    }

    override fun animationPlay(animate: Boolean) {
        behavior.animationPlay(animate)
    }

    override fun animationPause(animate: Boolean) {
        behavior.animationPause(animate)
    }

}
