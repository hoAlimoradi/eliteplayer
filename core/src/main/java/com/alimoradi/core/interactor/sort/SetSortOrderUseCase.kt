package com.alimoradi.core.interactor.sort

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.entity.sort.SortType
import com.alimoradi.core.prefs.SortPreferences
import javax.inject.Inject

class SetSortOrderUseCase @Inject constructor(
    private val gateway: SortPreferences
) {

    class Request(
        val mediaId: MediaId,
        val sortType: SortType
    )

    operator fun invoke(param: Request) {
        val category = param.mediaId.category
        return when (category) {
            MediaIdCategory.FOLDERS -> gateway.setDetailFolderSort(param.sortType)
            MediaIdCategory.PLAYLISTS,
            MediaIdCategory.PODCASTS_PLAYLIST -> gateway.setDetailPlaylistSort(param.sortType)
            MediaIdCategory.ALBUMS,
            MediaIdCategory.PODCASTS_ALBUMS -> gateway.setDetailAlbumSort(param.sortType)
            MediaIdCategory.ARTISTS,
            MediaIdCategory.PODCASTS_ARTISTS -> gateway.setDetailArtistSort(param.sortType)
            MediaIdCategory.GENRES -> gateway.setDetailGenreSort(param.sortType)
            else -> throw IllegalArgumentException("invalid param $param")
        }
    }
}