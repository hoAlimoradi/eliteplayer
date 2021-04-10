package com.alimoradi.presentation.tab.layoutmanager

class SongSpanSizeLookup(
    requestedSpanSize: Int
) : AbsSpanSizeLookup(requestedSpanSize) {

    override fun getSpanSize(position: Int): Int = getSpanCount() / requestedSpanSize
}