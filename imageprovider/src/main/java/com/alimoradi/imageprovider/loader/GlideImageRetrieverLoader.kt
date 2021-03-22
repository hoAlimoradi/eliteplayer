package com.alimoradi.imageprovider.loader

import android.content.Context
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.alimoradi.core.MediaId
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.gateway.ImageRetrieverGateway
import com.alimoradi.imageprovider.fetcher.GlideAlbumFetcher
import com.alimoradi.imageprovider.fetcher.GlideArtistFetcher
import com.alimoradi.imageprovider.fetcher.GlideSongFetcher
import java.io.InputStream
import javax.inject.Inject

internal class GlideImageRetrieverLoader(
    private val context: Context,
    private val imageRetrieverGateway: ImageRetrieverGateway
) : ModelLoader<MediaId, InputStream> {

    override fun handles(mediaId: MediaId): Boolean {
        if (mediaId.isAnyPodcast) {
            return false
        }
        return mediaId.isLeaf || mediaId.isAlbum || mediaId.isArtist
    }

    override fun buildLoadData(
        mediaId: MediaId,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {

        return if (mediaId.isLeaf) {
            // download track image
            ModelLoader.LoadData(
                MediaIdKey(mediaId),
                GlideSongFetcher(
                    context,
                    mediaId,
                    imageRetrieverGateway
                )
            )
        } else if (mediaId.isAlbum) {
            // download album image
            ModelLoader.LoadData(
                MediaIdKey(mediaId),
                GlideAlbumFetcher(
                    context,
                    mediaId,
                    imageRetrieverGateway
                )
            )
        } else {
            // download artist image
            ModelLoader.LoadData(
                MediaIdKey(mediaId),
                GlideArtistFetcher(
                    context,
                    mediaId,
                    imageRetrieverGateway
                )
            )
        }
    }

    class Factory @Inject constructor(
        @ApplicationContext private val context: Context,
        private val imageRetrieverGateway: ImageRetrieverGateway

    ) : ModelLoaderFactory<MediaId, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<MediaId, InputStream> {
            return GlideImageRetrieverLoader(
                context,
                imageRetrieverGateway
            )
        }

        override fun teardown() {
        }
    }

}