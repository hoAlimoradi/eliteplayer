package com.alimoradi.imageprovider.loader

import android.content.Context
import android.net.Uri
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.alimoradi.core.MediaId
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.gateway.track.FolderGateway
import com.alimoradi.core.gateway.track.GenreGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import com.alimoradi.core.prefs.AppPreferencesGateway
import com.alimoradi.imageprovider.fetcher.GlideMergedImageFetcher
import java.io.InputStream
import javax.inject.Inject

class GlideMergedImageLoader(
    private val context: Context,
    private val uriLoader: ModelLoader<Uri, InputStream>,
    private val folderGateway: FolderGateway,
    private val playlistGateway: PlaylistGateway,
    private val genreGateway: GenreGateway,
    private val prefsGateway: AppPreferencesGateway
) : ModelLoader<MediaId, InputStream> {

    override fun handles(mediaId: MediaId): Boolean {
        if (mediaId.isLeaf) {
            return false
        }
        return mediaId.isFolder || mediaId.isPlaylist || mediaId.isGenre || mediaId.isPodcastPlaylist
    }

    override fun buildLoadData(
        mediaId: MediaId,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? {
        if (!prefsGateway.canAutoCreateImages()) {
//             skip
            return uriLoader.buildLoadData(Uri.EMPTY, width, height, options)
        }

        return ModelLoader.LoadData(
            MediaIdKey(mediaId),
            GlideMergedImageFetcher(
                context,
                mediaId,
                folderGateway,
                playlistGateway,
                genreGateway
            )
        )
    }

    class Factory @Inject constructor(
        @ApplicationContext private val context: Context,
        private val folderGateway: FolderGateway,
        private val playlistGateway: PlaylistGateway,
        private val genreGateway: GenreGateway,
        private val prefsGateway: AppPreferencesGateway
    ) : ModelLoaderFactory<MediaId, InputStream> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<MediaId, InputStream> {
            val uriLoader = multiFactory.build(Uri::class.java, InputStream::class.java)
            return GlideMergedImageLoader(
                context,
                uriLoader,
                folderGateway,
                playlistGateway,
                genreGateway,
                prefsGateway
            )
        }

        override fun teardown() {

        }
    }
}

