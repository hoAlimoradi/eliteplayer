package com.alimoradi.servicemusic.utils

import android.support.v4.media.MediaMetadataCompat

fun MediaMetadataCompat.Builder.putBoolean(
    key: String,
    value: Boolean
): MediaMetadataCompat.Builder {
    putLong(key, if (value) 1 else 0)
    return this
}

