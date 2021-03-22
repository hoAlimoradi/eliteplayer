package com.alimoradi.imageprovider.fetcher

import android.content.Context
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.AutoPlaylist
import com.alimoradi.core.gateway.track.FolderGateway
import com.alimoradi.core.gateway.track.GenreGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import com.alimoradi.imageprovider.creator.ImagesFolderUtils
import com.alimoradi.imageprovider.creator.MergedImagesCreator
import com.alimoradi.imageprovider.executor.GlideScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.lang.RuntimeException

class GlideMergedImageFetcher(
    private val context: Context,
    private val mediaId: MediaId,
    private val folderGateway: FolderGateway,
    private val playlistGateway: PlaylistGateway,
    private val genreGateway: GenreGateway
) : DataFetcher<InputStream>, CoroutineScope by GlideScope() {

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        launch {
            try {
                val inputStream = when {
                    mediaId.isFolder -> makeFolderImage(mediaId.categoryValue)
                    mediaId.isGenre -> makeGenreImage(mediaId.categoryId)
                    else -> makePlaylistImage(mediaId.categoryId)
                }
                callback.onDataReady(inputStream)
            } catch (ex: Throwable){
                callback.onLoadFailed(RuntimeException(ex))
            }
        }
    }


    private suspend fun makeFolderImage(folder: String): InputStream? {
//        val folderImage = ImagesFolderUtils.forFolder(context, dirPath) --contains current image
        val albumsId = folderGateway.getTrackListByParam(folder).map { it.albumId }

        val folderName = ImagesFolderUtils.FOLDER
        val normalizedPath = folder.replace(File.separator, "")

        val file = MergedImagesCreator.makeImages(
            context = context,
            albumIdList = albumsId,
            parentFolder = folderName,
            itemId = normalizedPath
        )
        return file?.inputStream()
    }

    private suspend fun makeGenreImage(genreId: Long): InputStream? {
//        ImagesFolderUtils.forGenre(context, id) --contains current image

        val albumsId = genreGateway.getTrackListByParam(genreId).map { it.albumId }

        val folderName = ImagesFolderUtils.GENRE
        val file = MergedImagesCreator.makeImages(
            context = context,
            albumIdList = albumsId,
            parentFolder = folderName,
            itemId = "$genreId"
        )
        return file?.inputStream()
    }

    private suspend fun makePlaylistImage(playlistId: Long): InputStream? {
        if (AutoPlaylist.isAutoPlaylist(playlistId)){
            return null
        }

//        ImagesFolderUtils.forPlaylist(context, id) --contains current image
        val albumsId = playlistGateway.getTrackListByParam(playlistId).map { it.albumId }

        val folderName = ImagesFolderUtils.PLAYLIST
        val file = MergedImagesCreator.makeImages(
            context = context,
            albumIdList = albumsId,
            parentFolder = folderName,
            itemId = "$playlistId"
        )
        return file?.inputStream()
    }

    override fun getDataClass(): Class<InputStream> = InputStream::class.java

    override fun getDataSource(): DataSource = DataSource.LOCAL

    override fun cleanup() {

    }

    override fun cancel() {
        cancel(null)
    }

}