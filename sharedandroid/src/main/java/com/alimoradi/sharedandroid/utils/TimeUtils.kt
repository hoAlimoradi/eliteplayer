@file:Suppress("NOTHING_TO_INLINE")

package com.alimoradi.sharedandroid.utils

import android.content.Context
import com.alimoradi.sharedandroid.R
import java.util.concurrent.TimeUnit

object TimeUtils {

    @JvmStatic
    inline fun formatMillis(context: Context, millis: Int): String {
        return formatMillis(context, millis.toLong())
    }

    @JvmStatic
    fun formatMillis(context: Context, millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis))

        if (hours == 0L) {
            return context.resources.getQuantityString(R.plurals.common_plurals_minutes, minutes.toInt(), minutes.toInt())
        } else {
            var result = context.resources.getQuantityString(R.plurals.common_plurals_hours, hours.toInt(), hours.toInt()) + " "
            result += context.resources.getQuantityString(R.plurals.common_plurals_minutes, minutes.toInt(), minutes.toInt())
            return result
        }
    }

    @JvmStatic
    fun timeAsMillis(hours: Int, minutes: Int, seconds: Int): Long {
        return TimeUnit.HOURS.toMillis(hours.toLong()) +
                TimeUnit.MINUTES.toMillis(minutes.toLong()) +
                TimeUnit.SECONDS.toMillis(seconds.toLong())
    }

}