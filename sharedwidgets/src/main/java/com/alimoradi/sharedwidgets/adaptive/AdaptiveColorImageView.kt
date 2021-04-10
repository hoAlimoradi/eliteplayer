package com.alimoradi.sharedwidgets.adaptive

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.alimoradi.shared.lazyFast

open class AdaptiveColorImageView(
        context: Context,
        attr: AttributeSet

) : AppCompatImageView(context, attr) {

    private val presenter by lazyFast {
        AdaptiveColorImageViewPresenter(
            context
        )
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (!isInEditMode){
            presenter.onNextImage(bm)
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (isInEditMode){
            return
        }

        presenter.onNextImage(drawable)
    }

    fun observeProcessorColors() = presenter.observeProcessorColors()
    fun observePaletteColors() = presenter.observePalette()

}