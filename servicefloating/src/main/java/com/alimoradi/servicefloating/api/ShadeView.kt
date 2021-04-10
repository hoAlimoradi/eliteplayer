
package com.alimoradi.servicefloating.api

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.alimoradi.servicefloating.R
import com.alimoradi.sharedandroid.extensions.setGone
import com.alimoradi.sharedandroid.extensions.setVisible


/**
 * Fullscreen `View` that appears behind the other visual elements in a [HoverView] and
 * darkens the background.
 */
internal class ShadeView constructor(
    context: Context
) : FrameLayout(context) {

    companion object {
        private const val FADE_DURATION = 200L
    }

    private var currentAnimation: ObjectAnimator? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_shade, this, true)
        alpha = 0f
        visibility = View.GONE
    }

    // fade int
    fun show() {
        currentAnimation?.cancel()
        currentAnimation = ObjectAnimator.ofFloat(this, "alpha", 1.0f).apply {
            duration = FADE_DURATION
            doOnStart { setVisible() }
        }
        currentAnimation!!.start()
    }

    // fade out
    fun hide() {
        currentAnimation?.cancel()

        currentAnimation = ObjectAnimator.ofFloat(this, "alpha", 0.0f).apply {
            duration = FADE_DURATION
            doOnEnd { setGone() }
        }
        currentAnimation!!.start()
    }

    fun hideImmediate() {
        visibility = View.GONE
    }
}
