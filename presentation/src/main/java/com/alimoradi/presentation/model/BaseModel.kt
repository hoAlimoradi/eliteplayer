package com.alimoradi.presentation.model

import dev.olog.core.MediaId

interface BaseModel {
    val type: Int
    val mediaId: MediaId
}