package com.alimoradi.imageprovider.loader

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.alimoradi.imageprovider.fetcher.AudioFileCoverFetcher
import com.alimoradi.imageprovider.model.AudioFileCover
import java.io.InputStream

class AudioFileCoverLoader :
    ModelLoader<AudioFileCover, InputStream> {

    override fun buildLoadData(model: AudioFileCover, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(
            ObjectKey(model),
            AudioFileCoverFetcher(model)
        )
    }

    override fun handles(model: AudioFileCover): Boolean = true

    class Factory :
        ModelLoaderFactory<AudioFileCover, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<AudioFileCover, InputStream> {
            return AudioFileCoverLoader()
        }

        override fun teardown() {
        }
    }

}