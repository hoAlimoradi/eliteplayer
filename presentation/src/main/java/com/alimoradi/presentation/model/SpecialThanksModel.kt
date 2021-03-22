package com.alimoradi.presentation.model

import com.alimoradi.core.MediaId

data class SpecialThanksModel(
    override val type: Int,
    override val mediaId: MediaId,
    val title: String,
    val image: Int
) : BaseModel