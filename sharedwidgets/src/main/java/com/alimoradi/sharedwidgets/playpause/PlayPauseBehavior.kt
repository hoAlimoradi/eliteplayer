package com.alimoradi.sharedwidgets.playpause

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.alimoradi.sharedwidgets.R
import com.alimoradi.sharedandroid.extensions.getAnimatedVectorDrawable

interface IPlayPauseBehavior {

    fun animationPlay(animate: Boolean)
    fun animationPause(animate: Boolean)
}

class PlayPauseBehaviorImpl(private val button: ImageButton):
    IPlayPauseBehavior {

    companion object {
        @JvmStatic
        private val TAG = "P:${PlayPauseBehaviorImpl::class.java.simpleName}"
    }

    private val context = button.context

    private val playAnimation = context.getAnimatedVectorDrawable(R.drawable.avd_playpause_play_to_pause)
    private val pauseAnimation = context.getAnimatedVectorDrawable(R.drawable.avd_playpause_pause_to_play)
    private val playIcon = ContextCompat.getDrawable(context, R.drawable.vd_playpause_play)
    private val pauseIcon = ContextCompat.getDrawable(context, R.drawable.vd_playpause_pause)

    init {
        button.setImageDrawable(playIcon)
    }

    override fun animationPlay(animate: Boolean) {
        val notSameDrawable = button.drawable !== pauseIcon
        Log.v(TAG, "animation pause $animate, same drawable=${!notSameDrawable}")

        if (animate && notSameDrawable){
            setAvd(playAnimation)
        } else {
            button.setImageDrawable(pauseIcon)
        }
    }

    override fun animationPause(animate: Boolean) {
        val notSameDrawable = button.drawable !== playIcon
        Log.v(TAG, "animation pause $animate, same drawable=${!notSameDrawable}")

        if (animate && notSameDrawable){
            setAvd(pauseAnimation)
        } else {
            button.setImageDrawable(playIcon)
        }
    }

    private fun setAvd(avd: AnimatedVectorDrawableCompat){
        button.setImageDrawable(avd)
        avd.start()
        avd.registerAnimationCallback(object : Animatable2Compat.AnimationCallback(){
            override fun onAnimationEnd(drawable: Drawable?) {
                // force to set not animated drawable
                when (drawable){
                    playAnimation -> button.setImageResource(R.drawable.vd_playpause_pause)
                    pauseAnimation -> button.setImageResource(R.drawable.vd_playpause_play)
                }
            }
        })
    }

}