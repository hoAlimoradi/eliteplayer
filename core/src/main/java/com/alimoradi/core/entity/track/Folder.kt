package com.alimoradi.core.entity.track

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory

data class Folder(
    val title: String,
    val path: String,
    val size: Int
) {


    fun getMediaId(): MediaId {
        return MediaId.createCategoryValue(MediaIdCategory.FOLDERS, this.path)
    }

}