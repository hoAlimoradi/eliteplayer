package com.alimoradi.core.entity

import com.alimoradi.core.MediaId

data class SearchResult(
    val mediaId: MediaId,
    val itemType: Int,
    val title: String
)