package com.alimoradi.core.interactor.sort

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.entity.sort.SortEntity
import com.alimoradi.core.prefs.SortPreferences
import javax.inject.Inject

class GetDetailSortUseCase @Inject constructor(
    private val gateway: SortPreferences

) {

    operator fun invoke(mediaId: MediaId): SortEntity {
        val category = mediaId.category
        return when (category) {
            MediaIdCategory.FOLDERS -> gateway.getDetailFolderSort()
            MediaIdCategory.PLAYLISTS,
            MediaIdCategory.PODCASTS_PLAYLIST -> gateway.getDetailPlaylistSort()
            MediaIdCategory.ALBUMS,
            MediaIdCategory.PODCASTS_ALBUMS -> gateway.getDetailAlbumSort()
            MediaIdCategory.ARTISTS,
            MediaIdCategory.PODCASTS_ARTISTS -> gateway.getDetailArtistSort()
            MediaIdCategory.GENRES -> gateway.getDetailGenreSort()
            else -> throw IllegalArgumentException("invalid media id $mediaId")
        }
    }

}