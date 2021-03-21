@file:Suppress("NOTHING_TO_INLINE")

package com.alimoradi.sharedandroid.extensions

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context

inline fun Context.getAppWidgetsIdsFor(clazz: Class<*>): IntArray {
    return AppWidgetManager.getInstance(this).getAppWidgetIds(ComponentName(this, clazz))
}