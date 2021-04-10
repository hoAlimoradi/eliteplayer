package com.alimoradi.presentation.translations

import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.adapter.DataBoundViewHolder
import com.alimoradi.presentation.base.adapter.SimpleAdapter
import com.alimoradi.presentation.navigator.NavigatorAbout
import kotlinx.android.synthetic.main.item_translations_contributor.view.*

class TranslationFragmentAdapter(
    data: MutableList<String>,
    private val navigator: NavigatorAbout
) : SimpleAdapter<String>(data) {

    override fun initViewHolderListeners(viewHolder: DataBoundViewHolder, viewType: Int) {
        if (viewType == R.layout.item_translations_help){
            viewHolder.itemView.setOnClickListener {
                navigator.requestTranslation()
            }
        }
    }

    override fun bind(holder: DataBoundViewHolder, item: String, position: Int) {
        if (holder.itemViewType == R.layout.item_translations_contributor) {
            holder.itemView.text.text = item
        }
    }

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> R.layout.item_translations_help
        1 -> R.layout.item_translations_header
        else -> R.layout.item_translations_contributor
    }
}