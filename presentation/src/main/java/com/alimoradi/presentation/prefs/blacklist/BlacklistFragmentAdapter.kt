package com.alimoradi.presentation.prefs.blacklist

import com.alimoradi.presentation.BindingsAdapter
import com.alimoradi.presentation.base.adapter.DataBoundViewHolder
import com.alimoradi.presentation.base.adapter.SimpleAdapter
import com.alimoradi.sharedandroid.extensions.toggleVisibility
import kotlinx.android.synthetic.main.dialog_blacklist_item.view.*

class BlacklistFragmentAdapter(
    data: List<BlacklistModel>
) : SimpleAdapter<BlacklistModel>(data.toMutableList()) {

    override fun getItemViewType(position: Int): Int = dataSet[position].type

    override fun initViewHolderListeners(viewHolder: DataBoundViewHolder, viewType: Int) {
        viewHolder.itemView.setOnClickListener {
            getItem(viewHolder.adapterPosition)?.let { item ->
                item.isBlacklisted = !item.isBlacklisted
                notifyItemChanged(viewHolder.adapterPosition)
            }
        }
    }

    override fun bind(holder: DataBoundViewHolder, item: BlacklistModel, position: Int) {
        holder.itemView.apply {
            BindingsAdapter.loadAlbumImage(holder.imageView!!, item.mediaId)
            scrim.toggleVisibility(item.isBlacklisted, true)
            firstText.text = item.title
            secondText.text = item.displayablePath
        }
    }

}