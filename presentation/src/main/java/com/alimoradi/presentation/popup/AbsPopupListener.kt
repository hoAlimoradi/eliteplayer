package com.alimoradi.presentation.popup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.PopupMenu
import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.PlaylistType
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.interactor.playlist.AddToPlaylistUseCase
import com.alimoradi.core.interactor.playlist.GetPlaylistsUseCase
import com.alimoradi.presentation.R
import com.alimoradi.presentation.navigator.Navigator
import com.alimoradi.presentation.utils.asHtml
import com.alimoradi.sharedandroid.FileProvider
import com.alimoradi.sharedandroid.extensions.toast
import com.alimoradi.shared.lazyFast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.net.Uri

abstract class AbsPopupListener(
    getPlaylistBlockingUseCase: GetPlaylistsUseCase,
    private val addToPlaylistUseCase: AddToPlaylistUseCase,
    private val podcastPlaylist: Boolean

) : PopupMenu.OnMenuItemClickListener {

    val playlists by lazyFast {
        getPlaylistBlockingUseCase.execute(
            if (podcastPlaylist) PlaylistType.PODCAST
            else PlaylistType.TRACK
        )
    }

    @SuppressLint("RxLeakedSubscription")
    protected fun onPlaylistSubItemClick(
        context: Context,
        itemId: Int,
        mediaId: MediaId,
        listSize: Int,
        title: String
    ) {
        playlists.firstOrNull { it.id == itemId.toLong() }?.run {
            GlobalScope.launch {
                try {
                    addToPlaylistUseCase(this@run, mediaId)
                    createSuccessMessage(
                        context,
                        itemId.toLong(),
                        mediaId,
                        listSize,
                        title
                    )
                } catch (ex: Throwable){
                    createErrorMessage(context)
                }
            }
        }
    }

    private suspend fun createSuccessMessage(
        context: Context,
        playlistId: Long,
        mediaId: MediaId,
        listSize: Int,
        title: String
    ) = withContext(Dispatchers.Main) {
        val playlist = playlists.first { it.id == playlistId }.title
        val message = if (mediaId.isLeaf) {
            context.getString(R.string.added_song_x_to_playlist_y, title, playlist)
        } else {
            context.resources.getQuantityString(
                R.plurals.xx_songs_added_to_playlist_y,
                listSize,
                listSize,
                playlist
            )
        }
        context.toast(message)
    }

    private suspend fun createErrorMessage(context: Context) = withContext(Dispatchers.Main) {
        context.toast(context.getString(R.string.popup_error_message))
    }

    protected fun share(activity: Activity, song: Song) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        val uri = FileProvider.getUriForPath(activity, song.path)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "audio/*"
        grantUriPermission(activity, intent, uri)
        try {
            if (intent.resolveActivity(activity.packageManager) != null) {
                val string = activity.getString(R.string.share_song_x, song.title)
                activity.startActivity(Intent.createChooser(intent, string.asHtml()))
            } else {
                activity.toast(R.string.song_not_shareable)
            }
        } catch (ex: Throwable) {
            ex.printStackTrace()
            activity.toast(R.string.song_not_shareable)
        }
    }

    private fun grantUriPermission(context: Context, intent: Intent, uri: Uri){
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val resInfoList = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            context.grantUriPermission(
                packageName,
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    protected fun viewInfo(navigator: Navigator, mediaId: MediaId) {
        navigator.toEditInfoFragment(mediaId)
    }

    protected fun viewAlbum(navigator: Navigator, mediaId: MediaId) {
        navigator.toDetailFragment(mediaId)
    }

    protected fun viewArtist(navigator: Navigator, mediaId: MediaId) {
        navigator.toDetailFragment(mediaId)
    }

    protected fun setRingtone(navigator: Navigator, mediaId: MediaId, song: Song) {
        navigator.toSetRingtoneDialog(mediaId, song.title, song.artist)
    }


}