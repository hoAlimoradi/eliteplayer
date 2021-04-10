@file:Suppress("NOTHING_TO_INLINE")

package com.alimoradi.sharedandroid.extensions

import android.content.Intent
import android.content.pm.PackageManager

inline fun PackageManager.isIntentSafe(intent: Intent): Boolean {
    return queryIntentActivities(intent, 0).isNotEmpty()
}