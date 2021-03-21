package com.alimoradi.presentation.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.alimoradi.media.model.PlayerRepeatMode
import com.alimoradi.presentation.R
import com.alimoradi.sharedandroid.extensions.colorAccent
import com.alimoradi.sharedandroid.extensions.getAnimatedVectorDrawable
import com.alimoradi.sharedandroid.extensions.isDarkMode
import com.alimoradi.shared.lazyFast
import com.alimoradi.sharedandroid.theme.hasPlayerAppearance
import com.alimoradi.sharedwidgets.ColorDelegateImpl
import com.alimoradi.sharedwidgets.IColorDelegate
import java.lang.IllegalStateException

class RepeatButton(
    context: Context,
    attrs: AttributeSet

) : AppCompatImageButton(context, attrs), IColorDelegate by ColorDelegateImpl {

    private var enabledColor: Int
    private var repeatMode = PlayerRepeatMode.NOT_SET

    private val playerAppearance by lazyFast { context.hasPlayerAppearance() }
    private val isDarkMode by lazyFast { context.isDarkMode() }

    init {
        setImageResource(R.drawable.vd_repeat)
        enabledColor = context.colorAccent()
        background = null
        if (!isInEditMode){
            val defaultColor = getDefaultColor(context, playerAppearance, isDarkMode)
            setColorFilter(defaultColor)
        }
    }

    fun cycle(state: PlayerRepeatMode) {
        if (this.repeatMode != state) {
            this.repeatMode = state
            when (state) {
                PlayerRepeatMode.NOT_SET -> throw IllegalStateException("value not valid $state")
                PlayerRepeatMode.NONE -> repeatNone()
                PlayerRepeatMode.ONE -> repeatOne()
                PlayerRepeatMode.ALL -> repeatAll()
            }
        }
    }

    fun updateSelectedColor(color: Int) {
        this.enabledColor = color

        if (repeatMode != PlayerRepeatMode.NONE) {
            setColorFilter(this.enabledColor)
        }
    }

    private fun repeatNone() {
        val defaultColor = getDefaultColor(context, playerAppearance, isDarkMode)
        animateAvd(defaultColor, R.drawable.repeat_hide_one, R.drawable.repeat_show)
    }

    private fun repeatOne() {
        animateAvd(enabledColor, R.drawable.repeat_hide, R.drawable.repeat_show_one)
    }

    private fun repeatAll() {
        animateAvd(enabledColor, R.drawable.repeat_hide, R.drawable.repeat_show)
    }

    private fun animateAvd(@ColorInt endColor: Int, @DrawableRes hideAnim: Int, @DrawableRes showAnim: Int) {
        val hideDrawable = context.getAnimatedVectorDrawable(hideAnim)
        setImageDrawable(hideDrawable)
        hideDrawable.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                val showDrawable = context.getAnimatedVectorDrawable(showAnim)
                setColorFilter(endColor)
                setImageDrawable(showDrawable)
                showDrawable.start()
            }
        })
        hideDrawable.start()
    }

}