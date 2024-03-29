package com.alimoradi.imageprovider.loader

import android.content.Context
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.alimoradi.core.MediaId
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.imageprovider.fetcher.GlideOriginalImageFetcher
import java.io.InputStream
import javax.inject.Inject

internal class GlideOriginalImageLoader(
    private val context: Context,
    private val songGateway: SongGateway,
    private val podcastGateway: PodcastGateway

) : ModelLoader<MediaId, InputStream> {

    override fun handles(mediaId: MediaId): Boolean {
        if (mediaId.isLeaf) {
            return true
        }
        if (mediaId.isAlbum || mediaId.isPodcastAlbum) {
            return true
        }

        return false
    }

    override fun buildLoadData(
        mediaId: MediaId,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {

        // retrieve image store on track
        return ModelLoader.LoadData(
            MediaIdKey(mediaId),
            GlideOriginalImageFetcher(
                context,
                mediaId,
                songGateway,
                podcastGateway
            )
        )
    }

    class Factory @Inject constructor(
        @ApplicationContext private val context: Context,
        private val songGateway: SongGateway,
        private val podcastGateway: PodcastGateway
    ) : ModelLoaderFactory<MediaId, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<MediaId, InputStream> {
            return GlideOriginalImageLoader(
                context,
                songGateway,
                podcastGateway
            )
        }

        override fun teardown() {
        }
    }

}