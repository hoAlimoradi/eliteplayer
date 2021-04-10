package com.alimoradi.presentation.tab.layoutmanager

import androidx.recyclerview.widget.GridLayoutManager
import com.alimoradi.presentation.model.SpanCountController

abstract class AbsSpanSizeLookup(
    var requestedSpanSize: Int
) : GridLayoutManager.SpanSizeLookup() {

    fun getSpanCount() = SpanCountController.SPAN_COUNT

}