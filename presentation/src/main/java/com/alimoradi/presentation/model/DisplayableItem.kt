package com.alimoradi.presentation.model

import android.content.res.Resources
import com.alimoradi.core.MediaId
import com.alimoradi.presentation.R
import com.alimoradi.shared.TextUtils

sealed class DisplayableItem(
    override val type: Int,
    override val mediaId: MediaId
) : BaseModel

data class DisplayableTrack(
    override val type: Int,
    override val mediaId: MediaId,
    val title: String,
    val artist: String,
    val album: String,
    val idInPlaylist: Int,
    val dataModified: Long

) : DisplayableItem(type, mediaId) {

    val subtitle = "$artist${TextUtils.MIDDLE_DOT_SPACED}$album"

}

data class DisplayableAlbum(
    override val type: Int,
    override val mediaId: MediaId,
    val title: String,
    val subtitle: String
) : DisplayableItem(type, mediaId) {

    companion object {
        @JvmStatic
        fun readableSongCount(resources: Resources, size: Int): String {
            if (size <= 0) {
                return ""
            }
            return resources.getQuantityString(R.plurals.common_plurals_song, size, size)
                .toLowerCase()
        }
    }

}

data class DisplayableHeader(
    override val type: Int,
    override val mediaId: MediaId,
    val title: String,
    val subtitle: String? = null,
    val visible: Boolean = true

) : DisplayableItem(type, mediaId)


data class DisplayableNestedListPlaceholder(
    override val type: Int,
    override val mediaId: MediaId
) : DisplayableItem(type, mediaId)