package com.alimoradi.presentation.dialogs.playlist.clear

import android.content.Context
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

class ClearPlaylistDialog : BaseDialog() {

    companion object {
        const val TAG = "ClearPlaylistDialog"
        const val ARGUMENTS_MEDIA_ID = "$TAG.arguments.media_id"
        const val ARGUMENTS_ITEM_TITLE = "$TAG.arguments.item_title"

        @JvmStatic
        fun newInstance(mediaId: MediaId, itemTitle: String): ClearPlaylistDialog {
            return ClearPlaylistDialog().withArguments(
                    ARGUMENTS_MEDIA_ID to mediaId.toString(),
                    ARGUMENTS_ITEM_TITLE to itemTitle
            )
        }
    }

    private val mediaId: MediaId by lazyFast {
        val mediaId = arguments!!.getString(ARGUMENTS_MEDIA_ID)!!
        MediaId.fromString(mediaId)
    }
    private val title by lazy { arguments!!.getString(ARGUMENTS_ITEM_TITLE) }

    @Inject lateinit var presenter: ClearPlaylistDialogPresenter

    override fun extendBuilder(builder: MaterialAlertDialogBuilder): MaterialAlertDialogBuilder {
        return builder.setTitle(R.string.popup_clear_playlist)
            .setMessage(createMessage().asHtml())
            .setPositiveButton(R.string.popup_positive_delete, null)
            .setNegativeButton(R.string.popup_negative_cancel, null)
    }

    override fun positionButtonAction(context: Context) {
        launch {
            var message: String
            try {
                presenter.execute(mediaId)
                message = successMessage(act)
            } catch (ex: Throwable) {
                ex.printStackTrace()
                message = failMessage(act)
            }
            act.toast(message)
            dismiss()

        }
    }

    private fun successMessage(context: Context): String {
        return context.getString(R.string.playlist_x_cleared, title)
    }

    private fun failMessage(context: Context): String {
        return context.getString(R.string.popup_error_message)
    }

    private fun createMessage() : String {
        return context!!.getString(R.string.remove_songs_from_playlist_y, title)
    }

}