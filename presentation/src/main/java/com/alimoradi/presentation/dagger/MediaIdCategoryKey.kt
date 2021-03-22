package com.alimoradi.presentation.dagger

import dagger.MapKey
import com.alimoradi.core.MediaIdCategory

@MapKey
annotation class MediaIdCategoryKey(
        val value: MediaIdCategory
)
