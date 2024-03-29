package com.alimoradi.presentation.ripple

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.alimoradi.presentation.widgets.parallax.ParallaxImageView
import com.alimoradi.sharedwidgets.ForegroundImageView
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RippleTarget(
    imageView: ImageView,
    private val fallbackColor: Int = 0x40_606060,
    private val darkAlpha: Float = .1f,
    private val lightAlpha: Float = .2f

) : DrawableImageViewTarget(imageView) {

    private var job: Job? = null

    override fun onResourceReady(drawable: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(drawable, transition)
        if (view is ForegroundImageView) {
            job?.cancel()
            job = GlobalScope.launch(Dispatchers.Default) {
                generateRipple(drawable)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private suspend fun generateRipple(drawable: Drawable) {
        val bitmap = drawable.toBitmap()
        yield()
        val palette = generatePalette(bitmap)
        yield()
        onGenerated(palette)
    }

    private suspend fun generatePalette(bitmap: Bitmap) =
        suspendCoroutine<Palette?> { continuation ->
            Palette.from(bitmap).clearFilters().generate {
                continuation.resume(it)
            }
        }

    private suspend fun onGenerated(palette: Palette?) = withContext(Dispatchers.Main) {
        if (view is ForegroundImageView) {

            view.foreground = RippleUtils.create(
                palette, darkAlpha,
                lightAlpha, fallbackColor, true
            )
        }
        if (view is ParallaxImageView) {
            view.setScrimColor(
                RippleUtils.createColor(
                    palette,
                    darkAlpha,
                    lightAlpha,
                    fallbackColor
                )
            )
        }
    }
}