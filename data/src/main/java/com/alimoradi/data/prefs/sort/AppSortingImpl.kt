package com.alimoradi.data.prefs.sort

import android.content.SharedPreferences
import androidx.core.content.edit
import com.alimoradi.core.entity.sort.SortArranging
import com.alimoradi.core.entity.sort.SortEntity
import com.alimoradi.core.entity.sort.SortType
import com.alimoradi.core.prefs.SortDetail
import com.alimoradi.core.prefs.SortPreferences
import javax.inject.Inject

internal class AppSortingImpl @Inject constructor(
    private val preferences: SharedPreferences,
    private val detailSortingHelper: DetailSortingHelper

) : SortPreferences, SortDetail by detailSortingHelper {

    companion object {
        const val TAG = "AppPreferencesDataStoreImpl"

        private const val ALL_SONGS_SORT_ORDER = "$TAG.ALL_SONG_SORT_ORDER"
        private const val ALL_ALBUMS_SORT_ORDER = "$TAG.ALL_ALBUMS_SORT_ORDER"
        private const val ALL_ARTISTS_SORT_ORDER = "$TAG.ALL_ARTISTS_SORT_ORDER"

        private const val ALL_ALBUMS_SORT_ARRANGING = "$TAG.ALL_ALBUMS_SORT_ARRANGING"
        private const val ALL_SONGS_SORT_ARRANGING = "$TAG.ALL_SONGS_SORT_ARRANGING"
        private const val ALL_ARTISTS_SORT_ARRANGING = "$TAG.ALL_ARTISTS_SORT_ARRANGING"
    }

    override fun getAllTracksSort(): SortEntity {
        val sort = preferences.getInt(ALL_SONGS_SORT_ORDER, SortType.TITLE.ordinal)
        val arranging =
            preferences.getInt(ALL_SONGS_SORT_ARRANGING, SortArranging.ASCENDING.ordinal)
        return SortEntity(
            SortType.values()[sort],
            SortArranging.values()[arranging]
        )
    }

    override fun getAllAlbumsSort(): SortEntity {
        val sort = preferences.getInt(ALL_ALBUMS_SORT_ORDER, SortType.TITLE.ordinal)
        val arranging =
            preferences.getInt(ALL_ALBUMS_SORT_ARRANGING, SortArranging.ASCENDING.ordinal)
        return SortEntity(
            SortType.values()[sort],
            SortArranging.values()[arranging]
        )
    }

    override fun getAllArtistsSort(): SortEntity {
        val sort = preferences.getInt(ALL_ARTISTS_SORT_ORDER, SortType.ARTIST.ordinal)
        val arranging =
            preferences.getInt(ALL_ARTISTS_SORT_ARRANGING, SortArranging.ASCENDING.ordinal)
        return SortEntity(
            SortType.values()[sort],
            SortArranging.values()[arranging]
        )
    }

    override fun setAllTracksSort(sortType: SortEntity) {
        preferences.edit {
            putInt(ALL_SONGS_SORT_ORDER, sortType.type.ordinal)
            putInt(ALL_SONGS_SORT_ARRANGING, sortType.arranging.ordinal)
        }
    }

    override fun setAllAlbumsSort(sortType: SortEntity) {
        preferences.edit {
            putInt(ALL_ALBUMS_SORT_ORDER, sortType.type.ordinal)
            putInt(ALL_ALBUMS_SORT_ARRANGING, sortType.arranging.ordinal)
        }
    }

    override fun setAllArtistsSort(sortType: SortEntity) {
        preferences.edit {
            putInt(ALL_ARTISTS_SORT_ORDER, sortType.type.ordinal)
            putInt(ALL_ARTISTS_SORT_ARRANGING, sortType.arranging.ordinal)
        }
    }


}