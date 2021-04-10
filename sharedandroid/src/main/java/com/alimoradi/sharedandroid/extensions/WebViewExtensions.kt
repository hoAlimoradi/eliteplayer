package com.alimoradi.sharedandroid.extensions

import android.webkit.WebView
import com.alimoradi.sharedandroid.utils.isQ

fun WebView.setDarkMode(enable: Boolean) {
    if (isQ()) {
        isForceDarkAllowed = enable
    }
}