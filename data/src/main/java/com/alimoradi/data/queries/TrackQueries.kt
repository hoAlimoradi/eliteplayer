package com.alimoradi.data.queries

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore.Audio.Media.*
import dev.olog.contentresolversql.querySql
import com.alimoradi.core.entity.sort.SortArranging
import com.alimoradi.core.entity.sort.SortType
import com.alimoradi.core.gateway.base.Id
import com.alimoradi.core.prefs.BlacklistPreferences
import com.alimoradi.core.prefs.SortPreferences

@Suppress("DEPRECATION")
internal class TrackQueries(
    private val contentResolver: ContentResolver,
    blacklistPrefs: BlacklistPreferences,
    sortPrefs: SortPreferences,
    isPodcast: Boolean
) : BaseQueries(blacklistPrefs, sortPrefs, isPodcast) {

    fun getAll(): Cursor {
        val (blacklist, params) = notBlacklisted()

        val query = """
            SELECT $_ID, $ARTIST_ID, $ALBUM_ID,
                $TITLE,
                $ARTIST,
                $ALBUM,
                ${Columns.ALBUM_ARTIST},
                $DURATION,
                $DATA, $YEAR,
                $TRACK,
                $DATE_ADDED,
                $DATE_MODIFIED,
                $IS_PODCAST
            FROM $EXTERNAL_CONTENT_URI
            WHERE ${defaultSelection(blacklist)}
            ORDER BY ${sortOrder()}
        """

        return contentResolver.querySql(query, params)
    }

    fun getByParam(id: Id): Cursor {
        val (blacklist, params) = notBlacklisted()

        val query = """
            SELECT $_ID, $ARTIST_ID, $ALBUM_ID,
                $TITLE,
                $ARTIST,
                $ALBUM,
                ${Columns.ALBUM_ARTIST},
                $DURATION,
                $DATA, $YEAR,
                $TRACK,
                $DATE_ADDED,
                $DATE_MODIFIED,
                $IS_PODCAST
            FROM $EXTERNAL_CONTENT_URI
            WHERE $_ID = ? AND ${defaultSelection(blacklist)}
            ORDER BY ${sortOrder()}
        """

        return contentResolver.querySql(query, arrayOf("$id") + params)
    }

    private fun defaultSelection(notBlacklisted: String): String {
        return "${isPodcast()} AND $notBlacklisted"
    }

    private fun sortOrder(): String {
        if (isPodcast) {
            return "lower($TITLE) COLLATE UNICODE"
        }

        val sortEntity = sortPrefs.getAllTracksSort()
        var sort = when (sortEntity.type) {
            SortType.TITLE -> "lower($TITLE)"
            SortType.ARTIST -> "lower($ARTIST)"
            SortType.ALBUM -> "lower($ALBUM)"
            SortType.ALBUM_ARTIST -> "lower(${Columns.ALBUM_ARTIST})"
            SortType.DURATION -> DURATION
            SortType.RECENTLY_ADDED -> DATE_ADDED
            else -> "lower($TITLE)"
        }

        sort += " COLLATE UNICODE "

        if (sortEntity.type == SortType.RECENTLY_ADDED) {
            // recently added order works in reverse
            if (sortEntity.arranging == SortArranging.ASCENDING) {
                sort += " DESC"
            } else {
                sort += " ASC"
            }
        } else {
            if (sortEntity.arranging == SortArranging.ASCENDING) {
                sort += " ASC"
            } else {
                sort += " DESC"

            }
        }
        return sort
    }

}