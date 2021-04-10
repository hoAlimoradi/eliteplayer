package com.alimoradi.sharedandroid.theme

import android.content.Context

interface HasImmersive {
    fun isImmersive(): Boolean
}

fun Context.isImmersiveMode(): Boolean = (this.applicationContext as HasImmersive).isImmersive()