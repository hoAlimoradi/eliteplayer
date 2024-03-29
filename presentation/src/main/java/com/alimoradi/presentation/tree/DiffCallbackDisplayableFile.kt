package com.alimoradi.presentation.tree

import androidx.recyclerview.widget.DiffUtil
import com.alimoradi.presentation.model.DisplayableFile

internal object DiffCallbackDisplayableFile : DiffUtil.ItemCallback<DisplayableFile>() {
    override fun areItemsTheSame(oldItem: DisplayableFile, newItem: DisplayableFile): Boolean {
        return oldItem.mediaId == newItem.mediaId
    }

    override fun areContentsTheSame(oldItem: DisplayableFile, newItem: DisplayableFile): Boolean {
        return oldItem == newItem
    }
}