package com.alimoradi.presentation

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.imageprovider.CoverUtils
import com.alimoradi.imageprovider.GlideApp
import com.alimoradi.imageprovider.GlideUtils
import com.alimoradi.imageprovider.model.AudioFileCover
import com.alimoradi.presentation.model.DisplayableFile
import com.alimoradi.presentation.ripple.RippleTarget

object BindingsAdapter {

    @JvmStatic
    fun loadFile(view: ImageView, item: DisplayableFile) {
        val context = view.context
        GlideApp.with(context).clear(view)

        GlideApp.with(context)
                .load(AudioFileCover(item.path!!))
                .override(GlideUtils.OVERRIDE_SMALL)
                .placeholder(CoverUtils.getGradient(context, MediaId.songId(item.path.hashCode().toLong())))
                .into(view)
    }

    @JvmStatic
    fun loadDirImage(view: ImageView, item: DisplayableFile) {
        val mediaId = MediaId.createCategoryValue(MediaIdCategory.FOLDERS, item.path ?: "")
        loadImageImpl(
            view,
            mediaId,
            GlideUtils.OVERRIDE_SMALL
        )
    }

    @JvmStatic
    private fun loadImageImpl(
        view: ImageView,
        mediaId: MediaId,
        override: Int,
        priority: Priority = Priority.HIGH
    ) {
        val context = view.context

        GlideApp.with(context).clear(view)

        val builder = GlideApp.with(context)
            .load(mediaId)
            .override(override)
            .priority(priority)
            .placeholder(CoverUtils.getGradient(context, mediaId))
            .transition(DrawableTransitionOptions.withCrossFade())

        if (mediaId.isLeaf) {
            builder.into(view)
        } else {
            builder.into(RippleTarget(view))
        }
    }

    @JvmStatic
    fun loadSongImage(view: ImageView, mediaId: MediaId) {
        loadImageImpl(
            view,
            mediaId,
            GlideUtils.OVERRIDE_SMALL
        )
    }

    @JvmStatic
    fun loadAlbumImage(view: ImageView, mediaId: MediaId) {
        loadImageImpl(
            view,
            mediaId,
            GlideUtils.OVERRIDE_MID,
            Priority.HIGH
        )
    }

    @JvmStatic
    fun loadBigAlbumImage(view: ImageView, mediaId: MediaId) {
        val context = view.context

        GlideApp.with(context).clear(view)

        GlideApp.with(context)
            .load(mediaId)
            .override(GlideUtils.OVERRIDE_BIG)
            .priority(Priority.IMMEDIATE)
            .placeholder(CoverUtils.onlyGradient(context, mediaId))
            .error(CoverUtils.getGradient(context, mediaId))
            .onlyRetrieveFromCache(true)
            .into(RippleTarget(view))
    }

    @JvmStatic
    fun setBoldIfTrue(view: TextView, setBold: Boolean) {
        val style = if (setBold) Typeface.BOLD else Typeface.NORMAL
        view.setTypeface(null, style)
    }

}