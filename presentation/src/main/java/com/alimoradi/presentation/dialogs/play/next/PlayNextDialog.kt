package com.alimoradi.presentation.dialogs.play.next

import android.content.Context
import android.support.v4.media.session.MediaControllerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.alimoradi.core.MediaId
import com.alimoradi.presentation.R
import com.alimoradi.presentation.dialogs.BaseDialog
import com.alimoradi.presentation.utils.asHtml
import com.alimoradi.sharedandroid.extensions.act
import com.alimoradi.sharedandroid.extensions.toast
import com.alimoradi.sharedandroid.extensions.withArguments
import com.alimoradi.shared.lazyFast
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayNextDialog : BaseDialog() {

    companion object {
        const val TAG = "PlayNextDialog"
        const val ARGUMENTS_MEDIA_ID = "$TAG.arguments.media_id"
        const val ARGUMENTS_LIST_SIZE = "$TAG.arguments.list_size"
        const val ARGUMENTS_ITEM_TITLE = "$TAG.arguments.item_title"

        @JvmStatic
        fun newInstance(mediaId: MediaId, listSize: Int, itemTitle: String): PlayNextDialog {
            return PlayNextDialog().withArguments(
                    ARGUMENTS_MEDIA_ID to mediaId.toString(),
                    ARGUMENTS_LIST_SIZE to listSize,
                    ARGUMENTS_ITEM_TITLE to itemTitle
            )
        }
    }

    private val mediaId: MediaId by lazyFast {
        val mediaId = arguments!!.getString(ARGUMENTS_MEDIA_ID)!!
        MediaId.fromString(mediaId)
    }
    private val title: String by lazyFast { arguments!!.getString(ARGUMENTS_ITEM_TITLE)!! }
    private val listSize: Int by lazyFast { arguments!!.getInt(ARGUMENTS_LIST_SIZE) }

    @Inject lateinit var presenter: PlayNextDialogPresenter

    override fun extendBuilder(builder: MaterialAlertDialogBuilder): MaterialAlertDialogBuilder {
        return builder.setTitle(R.string.popup_play_next)
            .setMessage(createMessage().asHtml())
            .setPositiveButton(R.string.popup_positive_ok, null)
            .setNegativeButton(R.string.popup_negative_cancel, null)
    }

    private fun successMessage(context: Context): String {
        return if (mediaId.isLeaf){
            context.getString(R.string.song_x_added_to_play_next, title)
        } else context.resources.getQuantityString(R.plurals.xx_songs_added_to_play_next, listSize, listSize)
    }

    private  fun failMessage(context: Context): String {
        return context.getString(R.string.popup_error_message)
    }

    override fun positionButtonAction(context: Context) {
        launch {
            var message: String
            try {
                val mediaController = MediaControllerCompat.getMediaController(act)
                presenter.execute(mediaController, mediaId)
                message = successMessage(act)
            } catch (ex: Throwable) {
                ex.printStackTrace()
                message = failMessage(act)
            }
            act.toast(message)
            dismiss()
        }
    }

    private fun createMessage() : String {
        if (mediaId.isAll || mediaId.isLeaf){
            return getString(R.string.add_song_x_to_play_next, title)
        }
        return context!!.resources.getQuantityString(R.plurals.add_xx_songs_to_play_next, listSize, listSize)
    }

}