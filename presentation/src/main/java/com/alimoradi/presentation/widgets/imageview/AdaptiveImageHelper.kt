package com.alimoradi.presentation.widgets.imageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.alimoradi.shared.lazyFast
import com.alimoradi.sharedwidgets.adaptive.AdaptiveColorImageViewPresenter

internal class AdaptiveImageHelper(context: Context) {

    private val presenter by lazyFast {
        AdaptiveColorImageViewPresenter(
            context
        )
    }

    fun setImageBitmap(bm: Bitmap?) {
        presenter.onNextImage(bm)
    }

    fun setImageDrawable(drawable: Drawable?) {
        presenter.onNextImage(drawable)
    }

    fun observeProcessorColors() = presenter.observeProcessorColors()
    fun observePaletteColors() = presenter.observePalette()

}