package com.alimoradi.presentation.chooser

import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.alimoradi.appshortcuts.AppShortcuts
import com.alimoradi.presentation.BindingsAdapter
import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.adapter.DataBoundViewHolder
import com.alimoradi.presentation.base.adapter.DiffCallbackDisplayableItem
import com.alimoradi.presentation.base.adapter.ObservableAdapter
import com.alimoradi.presentation.base.adapter.setOnClickListener
import com.alimoradi.presentation.model.DisplayableAlbum
import com.alimoradi.presentation.model.DisplayableItem
import kotlinx.android.synthetic.main.item_tab_album.view.*

class PlaylistChooserActivityAdapter(
    private val activity: FragmentActivity

) : ObservableAdapter<DisplayableItem>(
    activity.lifecycle,
    DiffCallbackDisplayableItem
) {

    override fun initViewHolderListeners(viewHolder: DataBoundViewHolder, viewType: Int) {
        viewHolder.setOnClickListener(this) { item, _, _ ->
            askConfirmation(item)
        }
    }

    private fun askConfirmation(item: DisplayableItem) {
        require(item is DisplayableAlbum)

        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.playlist_chooser_dialog_title)
            .setMessage(activity.getString(R.string.playlist_chooser_dialog_message, item.title))
            .setPositiveButton(R.string.popup_positive_ok) { _, _ ->
                AppShortcuts.instance(activity).addDetailShortcut(item.mediaId, item.title)
                activity.finish()
            }
            .setNegativeButton(R.string.popup_negative_no, null)
            .show()
    }

    override fun bind(holder: DataBoundViewHolder, item: DisplayableItem, position: Int) {
        require(item is DisplayableAlbum)

        holder.itemView.apply {
            BindingsAdapter.loadAlbumImage(holder.imageView!!, item.mediaId)
            firstText.text = item.title
            secondText.text = item.subtitle
        }
    }
}