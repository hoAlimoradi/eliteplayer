package com.alimoradi.presentation.model

import android.content.Context
import com.alimoradi.presentation.tab.TabCategory
import com.alimoradi.sharedandroid.extensions.configuration
import com.alimoradi.shared.throwNotHandled

internal object SpanCountController {

    const val SPAN_COUNT = 60

    @JvmStatic
    fun getDefaultSpan(context: Context, category: TabCategory): Int {
        val smallestWidthDip = context.configuration.smallestScreenWidthDp
        val isTablet = smallestWidthDip >= 600
        return when (category) {
            TabCategory.FOLDERS -> if (isTablet) 4 else 3
            TabCategory.PLAYLISTS,
            TabCategory.PODCASTS_PLAYLIST -> if (isTablet) 4 else 3
            TabCategory.SONGS,
            TabCategory.PODCASTS -> 1
            TabCategory.ALBUMS,
            TabCategory.PODCASTS_ALBUMS -> if (isTablet) 4 else 2
            TabCategory.ARTISTS,
            TabCategory.PODCASTS_ARTISTS -> if (isTablet) 4 else 3
            TabCategory.GENRES -> if (isTablet) 4 else 3
            else -> throwNotHandled("invalid $category")
        }
    }

}