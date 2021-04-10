package com.alimoradi.presentation.model

import com.alimoradi.core.MediaId

interface BaseModel {
    val type: Int
    val mediaId: MediaId
}