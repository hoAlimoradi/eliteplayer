package com.alimoradi.presentation.dialogs.playlist.duplicates

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

class RemoveDuplicatesDialog: BaseDialog() {

    companion object {
        const val TAG = "RemoveDuplicatesDialog"
        const val ARGUMENTS_MEDIA_ID = "$TAG.arguments.media_id"
        const val ARGUMENTS_ITEM_TITLE = "$TAG.arguments.item_title"

        @JvmStatic
        fun newInstance(mediaId: MediaId, itemTitle: String): RemoveDuplicatesDialog {
            return RemoveDuplicatesDialog().withArguments(
                    ARGUMENTS_MEDIA_ID to mediaId.toString(),
                    ARGUMENTS_ITEM_TITLE to itemTitle
            )
        }
    }

    @Inject lateinit var presenter: RemoveDuplicatesDialogPresenter


    private val itemTitle by lazyFast { arguments!!.getString(ARGUMENTS_ITEM_TITLE) }

    override fun extendBuilder(builder: MaterialAlertDialogBuilder): MaterialAlertDialogBuilder {
        return builder.setTitle(R.string.remove_duplicates_title)
            .setMessage(createMessage().asHtml())
            .setPositiveButton(R.string.popup_positive_remove, null)
            .setNegativeButton(R.string.popup_negative_no, null)
    }

    override fun positionButtonAction(context: Context) {
        launch {
            var message: String
            try {
                val mediaId = MediaId.fromString(arguments!!.getString(ARGUMENTS_MEDIA_ID)!!)
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
        return context.getString(R.string.remove_duplicates_success, itemTitle)
    }

    private fun failMessage(context: Context): String {
        return context.getString(R.string.popup_error_message)
    }

    private fun createMessage() : String {
        return context!!.getString(R.string.remove_duplicates_message, itemTitle)
    }

}