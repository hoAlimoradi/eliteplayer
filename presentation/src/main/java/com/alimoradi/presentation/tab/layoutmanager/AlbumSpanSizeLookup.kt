package com.alimoradi.presentation.tab.layoutmanager

import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.adapter.ObservableAdapter
import com.alimoradi.presentation.model.BaseModel

class AlbumSpanSizeLookup(
    private val adapter: ObservableAdapter<BaseModel>,
    requestedSpanSize: Int
) : AbsSpanSizeLookup(requestedSpanSize) {


    override fun getSpanSize(position: Int): Int {
        when (adapter.getItem(position)!!.type) {
            R.layout.item_tab_header,
            R.layout.item_tab_new_album_horizontal_list,
            R.layout.item_tab_last_played_album_horizontal_list -> return getSpanCount()
        }

        return getSpanCount() / requestedSpanSize
    }

}