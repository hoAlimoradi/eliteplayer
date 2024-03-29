package com.alimoradi.presentation.recentlyadded

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.alimoradi.media.MediaProvider
import com.alimoradi.presentation.BindingsAdapter
import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.adapter.*
import com.alimoradi.presentation.base.drag.IDragListener
import com.alimoradi.presentation.base.drag.TouchableAdapter
import com.alimoradi.presentation.model.DisplayableItem
import com.alimoradi.presentation.model.DisplayableTrack
import com.alimoradi.presentation.navigator.Navigator
import kotlinx.android.synthetic.main.item_recently_added.view.*

class RecentlyAddedFragmentAdapter(
    lifecycle: Lifecycle,
    private val navigator: Navigator,
    private val mediaProvider: MediaProvider,
    private val dragListener: IDragListener

) : ObservableAdapter<DisplayableItem>(
    lifecycle,
    DiffCallbackDisplayableItem
), TouchableAdapter {

    override fun initViewHolderListeners(viewHolder: DataBoundViewHolder, viewType: Int) {
        viewHolder.setOnClickListener(this) { item, _, _ ->
            mediaProvider.playFromMediaId(item.mediaId, null, null)
        }
        viewHolder.setOnLongClickListener(this) { item, _, _ ->
            navigator.toDialog(item.mediaId, viewHolder.itemView)
        }
        viewHolder.setOnClickListener(R.id.more, this) { item, _, view ->
            navigator.toDialog(item.mediaId, view)
        }
        viewHolder.elevateAlbumOnTouch()
        viewHolder.setOnDragListener(R.id.dragHandle, dragListener)
    }

    override fun bind(holder: DataBoundViewHolder, item: DisplayableItem, position: Int) {
        require(item is DisplayableTrack)

        holder.itemView.apply {
            BindingsAdapter.loadSongImage(holder.imageView!!, item.mediaId)
            firstText.text = item.title
            secondText.text = item.subtitle
            explicit.onItemChanged(item.title)
        }
    }

    override fun canInteractWithViewHolder(viewType: Int): Boolean {
        return viewType == R.layout.item_recently_added
    }

    override fun afterSwipeLeft(viewHolder: RecyclerView.ViewHolder) {
        val item = getItem(viewHolder.adapterPosition)!!
        mediaProvider.addToPlayNext(item.mediaId)
    }

}