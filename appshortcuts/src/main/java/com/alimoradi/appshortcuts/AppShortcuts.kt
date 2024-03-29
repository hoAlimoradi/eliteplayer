package com.alimoradi.appshortcuts

import android.content.Context
import com.alimoradi.core.MediaId

class AppShortcuts private constructor(context: Context) {

    private val appShortcuts = AppShortcutsImp(context.applicationContext)

    companion object {
        @JvmStatic
        private var instance: AppShortcuts? = null

        @JvmStatic
        fun instance(context: Context): AppShortcuts {
            if (instance == null) {
                instance = AppShortcuts(context)
            }
            return instance!!
        }
    }

    fun addDetailShortcut(mediaId: MediaId, title: String) {
        appShortcuts.addDetailShortcut(mediaId, title)
    }

}