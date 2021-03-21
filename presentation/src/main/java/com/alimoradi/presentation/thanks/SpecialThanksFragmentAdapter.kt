package com.alimoradi.presentation.thanks

import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import com.alimoradi.imageprovider.GlideApp
import com.alimoradi.presentation.base.adapter.DataBoundViewHolder
import com.alimoradi.presentation.base.adapter.ObservableAdapter
import com.alimoradi.presentation.model.SpecialThanksModel
import kotlinx.android.synthetic.main.item_special_thanks.view.*

class SpecialThanksFragmentAdapter(
    lifecycle: Lifecycle
) : ObservableAdapter<SpecialThanksModel>(lifecycle,
    DiffUtilSpecialThansModel
) {

    override fun initViewHolderListeners(viewHolder: DataBoundViewHolder, viewType: Int) {
    }

    override fun bind(holder: DataBoundViewHolder, item: SpecialThanksModel, position: Int) {
        holder.itemView.apply {
            GlideApp.with(context)
                .load(ContextCompat.getDrawable(context, item.image))
                .into(image)

            title.text = item.title
        }
    }

}

object DiffUtilSpecialThansModel : DiffUtil.ItemCallback<SpecialThanksModel>() {
    override fun areItemsTheSame(
        oldItem: SpecialThanksModel,
        newItem: SpecialThanksModel
    ): Boolean {
        return oldItem.mediaId == newItem.mediaId
    }

    override fun areContentsTheSame(
        oldItem: SpecialThanksModel,
        newItem: SpecialThanksModel
    ): Boolean {
        return oldItem == newItem
    }
}