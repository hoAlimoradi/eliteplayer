package com.alimoradi.presentation.detail.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import com.alimoradi.core.entity.sort.SortArranging
import com.alimoradi.core.entity.sort.SortEntity
import com.alimoradi.core.entity.sort.SortType
import com.alimoradi.presentation.R

internal class SortButton(
    context: Context,
    attrs: AttributeSet
) : AppCompatImageButton(context, attrs) {

    fun update(sortEntity: SortEntity) {
        if (sortEntity.type == SortType.CUSTOM) {
            setImageResource(R.drawable.vd_remove)
        } else {
            if (sortEntity.arranging == SortArranging.ASCENDING) {
                setImageResource(R.drawable.vd_arrow_down)
            } else {
                setImageResource(R.drawable.vd_arrow_up)
            }
        }
    }

}