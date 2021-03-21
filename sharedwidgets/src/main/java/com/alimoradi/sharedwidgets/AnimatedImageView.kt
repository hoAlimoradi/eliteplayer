package com.alimoradi.sharedwidgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewPropertyAnimator
import androidx.appcompat.widget.AppCompatImageButton
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.alimoradi.sharedandroid.extensions.getAnimatedVectorDrawable
import com.alimoradi.sharedandroid.extensions.isDarkMode
import com.alimoradi.sharedandroid.theme.hasPlayerAppearance
import com.alimoradi.shared.lazyFast

class AnimatedImageView(
    context: Context,
    attrs: AttributeSet

) : AppCompatImageButton(context, attrs), IColorDelegate by ColorDelegateImpl {

    private val playerAppearance by lazyFast { context.hasPlayerAppearance() }

    private val avd: AnimatedVectorDrawableCompat
    private val animator: ViewPropertyAnimator = animate()

    private val isDarkMode by lazyFast { context.isDarkMode() }

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs, R.styleable.AnimatedImageView, 0, 0
        )

        val resId = a.getResourceId(R.styleable.AnimatedImageView_avd, -1)
        avd = context.getAnimatedVectorDrawable(resId)
        setImageDrawable(avd)
        a.recycle()
    }

    fun setDefaultColor() {
        val defaultColor = getDefaultColor(context, playerAppearance, isDarkMode)
        setColorFilter(defaultColor)
    }

    fun playAnimation() {
        stopPreviousAnimation()
        avd.start()
    }

    private fun stopPreviousAnimation() {
        avd.stop()
    }

    fun updateVisibility(show: Boolean) {
        isEnabled = show

        animator.cancel()
        animator.alpha(if (show) 1f else 0f)
    }

}
