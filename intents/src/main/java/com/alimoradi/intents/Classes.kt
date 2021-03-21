package com.alimoradi.intents

object Classes {
    const val ACTIVITY_MAIN = "com.alimoradi.presentation.main.MainActivity"
    const val ACTIVITY_SHORTCUTS = "com.alimoradi.appshortcuts.ShortcutsActivity"
    const val ACTIVITY_PLAYLIST_CHOOSER = "com.alimoradi.presentation.playlist.chooser.PlaylistChooserActivity"

    const val SERVICE_MUSIC = "com.alimoradi.servicemusic.MusicService"
    const val SERVICE_FLOATING = "com.alimoradi.servicefloating.FloatingWindowService"

    const val WIDGET_COLORED = "com.alimoradi.eliteplayer.appwidgets.WidgetColored"

    @JvmStatic
    val widgets: List<Class<*>> by lazy {
        listOf(
            Class.forName(WIDGET_COLORED)
        )
    }

}