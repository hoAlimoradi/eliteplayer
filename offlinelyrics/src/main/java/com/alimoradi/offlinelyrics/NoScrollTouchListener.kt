package com.alimoradi.offlinelyrics

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Math.abs

class NoScrollTouchListener(
    context: Context,
    private val action: () -> Unit
) : View.OnTouchListener {

    companion object {
        private const val TOUCH_DELAY = 3000L
    }

    var userHasControl: Boolean = false
        private set

    private var job: Job? = null

    private val configuration = ViewConfiguration.get(context)

    private var timePressed = -1L
    private var xDown = -1f
    private var yDown = -1f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                timePressed = System.currentTimeMillis()
                xDown = event.x
                yDown = event.y

                return false
            }
            MotionEvent.ACTION_MOVE -> {
                val diffX = abs(event.x - xDown)
                val diffY = abs(event.x - xDown)

                if ((diffX > 25 || diffY > 25)) {
                    userHasControl = true
                    job?.cancel()
                    job = GlobalScope.launch {
                        delay(TOUCH_DELAY)
                        userHasControl = false
                    }
                }

                return false
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                if (System.currentTimeMillis() - timePressed <= ViewConfiguration.getTapTimeout()) {
                    val xUp = event.x
                    val yUp = event.y
                    if (abs(xUp - xDown) < configuration.scaledTouchSlop &&
                        abs(yUp - yDown) < configuration.scaledTouchSlop
                    ) {
                        action()
                        return false
                    }
                }
            }
        }
        return false
    }
}