package com.alimoradi.presentation.utils

import android.text.Html
import android.text.Spanned
import com.alimoradi.sharedandroid.utils.isNougat

fun String.asHtml(): Spanned {
    return if (isNougat()){
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}