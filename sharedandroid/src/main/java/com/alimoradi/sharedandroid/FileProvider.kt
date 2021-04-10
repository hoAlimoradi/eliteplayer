package com.alimoradi.sharedandroid

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object FileProvider {

    private const val authority = "com.alimoradi.eliteplayer.fileprovider"

    @JvmStatic
    fun getUriForFile(context: Context, file: File): Uri {
        return try {
            FileProvider.getUriForFile(context, authority, file)
        } catch (ex: Throwable) {
            ex.printStackTrace()
            return Uri.EMPTY
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    @JvmStatic
    inline fun getUriForPath(context: Context, path: String): Uri {
        return getUriForFile(context, File(path))
    }

}