package com.alimoradi.presentation.detail.adapter

import androidx.lifecycle.Lifecycle
import com.alimoradi.presentation.BindingsAdapter
import com.alimoradi.presentation.base.adapter.*
import com.alimoradi.presentation.model.DisplayableAlbum
import com.alimoradi.presentation.model.DisplayableItem
import com.alimoradi.presentation.navigator.Navigator
import kotlinx.android.synthetic.main.item_detail_related_artist.view.*

class DetailRelatedArtistsAdapter(
    lifecycle: Lifecycle,
    private val navigator: Navigator

) : ObservableAdapter<DisplayableItem>(
    lifecycle,
    DiffCallbackDisplayableItem
) {

    override fun initViewHolderListeners(viewHolder: DataBoundViewHolder, viewType: Int) {
        viewHolder.setOnClickListener(this) { item, _, _ ->
            navigator.toDetailFragment(item.mediaId)
        }
        viewHolder.setOnLongClickListener(this) { item, _, _ ->
            navigator.toDialog(item.mediaId, viewHolder.itemView)
        }
        viewHolder.elevateAlbumOnTouch()
    }

    override fun bind(holder: DataBoundViewHolder, item: DisplayableItem, position: Int) {
        require(item is DisplayableAlbum)

        holder.itemView.apply {
            BindingsAdapter.loadAlbumImage(holder.imageView!!, item.mediaId)
            firstText.text = item.title
            secondText.text = item.subtitle
            quickAction.setId(item.mediaId)
        }
    }
}