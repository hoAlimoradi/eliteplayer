package com.alimoradi.presentation.dialogs.play.later

import android.support.v4.media.session.MediaControllerCompat
import androidx.core.os.bundleOf
import com.alimoradi.core.MediaId
import com.alimoradi.core.interactor.songlist.GetSongListByParamUseCase
import com.alimoradi.intents.MusicServiceCustomAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayLaterDialogPresenter @Inject constructor(
    private val getSongListByParamUseCase: GetSongListByParamUseCase
) {

    suspend fun execute(mediaController: MediaControllerCompat, mediaId: MediaId) = withContext(Dispatchers.IO) {
        val items = if (mediaId.isLeaf) {
            listOf(mediaId.leaf!!)
        } else {
            getSongListByParamUseCase(mediaId).map { it.id }
        }
        val bundle = bundleOf(
            MusicServiceCustomAction.ARGUMENT_IS_PODCAST to mediaId.isAnyPodcast,
            MusicServiceCustomAction.ARGUMENT_MEDIA_ID_LIST to items.toLongArray()
        )

        mediaController.transportControls.sendCustomAction(
            MusicServiceCustomAction.ADD_TO_PLAY_LATER.name,
            bundle
        )
    }

}