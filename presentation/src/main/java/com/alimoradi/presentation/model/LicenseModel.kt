package com.alimoradi.presentation.model

import dev.olog.core.MediaId

data class LicenseModel(
    override val type: Int,
    override val mediaId: MediaId,
    val name: String,
    val url: String,
    val license: String
) : BaseModel