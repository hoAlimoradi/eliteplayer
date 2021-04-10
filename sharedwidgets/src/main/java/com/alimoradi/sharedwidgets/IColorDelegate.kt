package com.alimoradi.sharedwidgets

import android.content.Context
import android.graphics.Color
import com.alimoradi.sharedandroid.extensions.colorControlNormal
import com.alimoradi.sharedandroid.theme.HasPlayerAppearance

interface IColorDelegate {

    fun getDefaultColor(
        context: Context,
        playerAppearance: HasPlayerAppearance,
        isDarkMode: Boolean
    ): Int

    fun lightColor(): Int

}

object ColorDelegateImpl : IColorDelegate {

    override fun getDefaultColor(
        context: Context,
        playerAppearance: HasPlayerAppearance,
        isDarkMode: Boolean
    ): Int {
        return when {
            playerAppearance.isFullscreen() || isDarkMode -> Color.WHITE
            else -> context.colorControlNormal()
        }
    }

    override fun lightColor(): Int {
        return 0xFF_F5F5F5.toInt()
    }
}