package com.alimoradi.imageprovider

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.DEFAULT
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.IGNORE
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import dev.olog.core.MediaId
import com.alimoradi.imageprovider.di.inject
import com.alimoradi.imageprovider.loader.AudioFileCoverLoader
import com.alimoradi.imageprovider.loader.GlideImageRetrieverLoader
import com.alimoradi.imageprovider.loader.GlideMergedImageLoader
import com.alimoradi.imageprovider.loader.GlideOriginalImageLoader
import com.alimoradi.imageprovider.model.AudioFileCover
import java.io.InputStream
import javax.inject.Inject

@GlideModule
@Keep
class GlideModule : AppGlideModule() {

    @Inject
    internal lateinit var lastFmFactory: GlideImageRetrieverLoader.Factory
    @Inject
    internal lateinit var originalFactory: GlideOriginalImageLoader.Factory
    @Inject
    internal lateinit var mergedFactory: GlideMergedImageLoader.Factory

    private var injected = false

    private fun injectIfNeeded(context: Context) {
        if (!injected) {
            injected = true
            inject(context)
        }
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val level = if (BuildConfig.DEBUG) DEFAULT else IGNORE
        builder.setLogLevel(Log.ERROR)
            .setDefaultRequestOptions(defaultRequestOptions(context))
            .setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor(level))
            .setSourceExecutor(GlideExecutor.newSourceExecutor(level))
    }

    private fun defaultRequestOptions(context: Context): RequestOptions {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        return RequestOptions()
            // Prefer higher quality images unless we're on a low RAM device
            .format(
                if (activityManager.isLowRamDevice)
                    DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888
            ).disallowHardwareConfig()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .centerCrop()
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        injectIfNeeded(context)

        registry.prepend(AudioFileCover::class.java, InputStream::class.java, AudioFileCoverLoader.Factory())

        registry.prepend(MediaId::class.java, InputStream::class.java, lastFmFactory)
        registry.prepend(MediaId::class.java, InputStream::class.java, mergedFactory)
        registry.prepend(MediaId::class.java, InputStream::class.java, originalFactory)
    }

    override fun isManifestParsingEnabled(): Boolean = false

}