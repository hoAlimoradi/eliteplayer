package com.alimoradi.data.queries

import android.provider.MediaStore.Audio.Media.*
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.entity.sort.SortArranging
import com.alimoradi.core.entity.sort.SortEntity
import com.alimoradi.core.entity.sort.SortType
import com.alimoradi.core.prefs.BlacklistPreferences
import com.alimoradi.core.prefs.SortPreferences
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
abstract class BaseQueries(
    protected val blacklistPrefs: BlacklistPreferences,
    protected val sortPrefs: SortPreferences,
    protected val isPodcast: Boolean
) {

    companion object {
        @JvmStatic
        private val RECENTLY_ADDED_TIME = TimeUnit.SECONDS.convert(14, TimeUnit.DAYS)
    }

    protected val folderProjection: String
        get() = "substr($DATA, 1, length($DATA) - length($DISPLAY_NAME) - 1)"

    private  val discNumberProjection = "CASE WHEN $TRACK >= 1000 THEN substr($TRACK, 1, 1) ELSE 0 END"
    private  val trackNumberProjection = "CASE WHEN $TRACK >= 1000 THEN $TRACK % 1000 ELSE $TRACK END"

    protected fun isPodcast(): String {
        return if (isPodcast) "$IS_PODCAST <> 0" else "$IS_PODCAST = 0"
    }

    protected fun isRecentlyAdded(): String {
        return "strftime('%s','now') - $DATE_ADDED <= $RECENTLY_ADDED_TIME"
    }

    protected fun notBlacklisted(): Pair<String, Array<String>> {
        val blacklist = blacklistPrefs.getBlackList()
        val params = blacklist.map { "?" }
        val blackListed = blacklist.toTypedArray()
        return "$folderProjection NOT IN (${params.joinToString()})" to blackListed
    }

    protected fun songListSortOrder(category: MediaIdCategory, default: String): String {

        val sortEntity = getSortType(category)
        var sort = when (sortEntity.type) {
            SortType.TITLE -> "lower($TITLE)"
            SortType.ARTIST -> "lower($ARTIST)"
            SortType.ALBUM -> "lower($ALBUM)"
            SortType.ALBUM_ARTIST -> "lower(${Columns.ALBUM_ARTIST})"
            SortType.RECENTLY_ADDED -> DATE_ADDED // DESC
            SortType.DURATION -> DURATION
            SortType.TRACK_NUMBER -> "$discNumberProjection ${sortEntity.arranging}, $trackNumberProjection ${sortEntity.arranging}, $TITLE"
            SortType.CUSTOM -> default
            else -> "lower($TITLE)"
        }

        if (sortEntity.type == SortType.CUSTOM) {
            return sort
        }

        sort += " COLLATE UNICODE "

        if (sortEntity.arranging == SortArranging.ASCENDING && sortEntity.type == SortType.RECENTLY_ADDED) {
            // recently added works in reverse
            sort += " DESC"
        }
        if (sortEntity.arranging == SortArranging.DESCENDING) {
            if (sortEntity.type == SortType.RECENTLY_ADDED) {
                // recently added works in reverse
                sort += " ASC"
            } else {
                sort += " DESC"
            }

        }
        return sort
    }

    private fun getSortType(category: MediaIdCategory): SortEntity {
        return when (category) {
            MediaIdCategory.FOLDERS -> sortPrefs.getDetailFolderSort()
            MediaIdCategory.PLAYLISTS -> sortPrefs.getDetailPlaylistSort()
            MediaIdCategory.ALBUMS -> sortPrefs.getDetailAlbumSort()
            MediaIdCategory.ARTISTS -> sortPrefs.getDetailArtistSort()
            MediaIdCategory.GENRES -> sortPrefs.getDetailFolderSort()
            else -> throw IllegalArgumentException("invalid category $category")
        }
    }

}