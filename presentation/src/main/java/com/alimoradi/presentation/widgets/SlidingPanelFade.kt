package com.alimoradi.presentation.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.math.MathUtils.clamp
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.alimoradi.presentation.R
import com.alimoradi.presentation.interfaces.HasSlidingPanel
import com.alimoradi.presentation.utils.collapse
import com.alimoradi.sharedandroid.extensions.dip
import com.alimoradi.sharedandroid.extensions.isTablet
import com.alimoradi.sharedandroid.extensions.scrimBackground
import com.alimoradi.shared.lazyFast

class SlidingPanelFade(
        context: Context,
        attrs: AttributeSet
) : View(context, attrs) {

    private val fragmentContainer by lazyFast {
        (context as FragmentActivity).findViewById<View>(R.id.fragmentContainer)
    }

    private val slidingPanel by lazyFast { (context as HasSlidingPanel).getSlidingPanel() }
    private val isTablet = context.isTablet

    var parallax = context.dip(20)

    init {
        setBackgroundColor(context.scrimBackground())
        alpha = 0f
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        slidingPanel.addPanelSlideListener(slidingPanelCallback)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        slidingPanel.removePanelSlideListener(slidingPanelCallback)
    }

    private val slidingPanelCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            alpha = clamp(slideOffset * 1.5f, 0f, 1f)

            if (!isTablet){
                fragmentContainer.translationY = -(slideOffset * parallax)
            }
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            val setClickable = newState != BottomSheetBehavior.STATE_COLLAPSED
            if (setClickable){
                setOnClickListener { slidingPanel.collapse() }
            } else {
                setOnClickListener(null)
            }

            isClickable = setClickable
            isFocusable = isClickable
        }
    }

}