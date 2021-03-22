package com.alimoradi.core.interactor.sort

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.entity.sort.SortEntity
import com.alimoradi.core.interactor.base.FlowUseCaseWithParam
import com.alimoradi.core.prefs.SortPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveDetailSortUseCase @Inject constructor(
    private val gateway: SortPreferences

) : FlowUseCaseWithParam<SortEntity, MediaId>() {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun buildUseCase(mediaId: MediaId): Flow<SortEntity> {
        return when (mediaId.category) {
            MediaIdCategory.FOLDERS -> gateway.observeDetailFolderSort()
            MediaIdCategory.PLAYLISTS,
            MediaIdCategory.PODCASTS_PLAYLIST -> gateway.observeDetailPlaylistSort()
            MediaIdCategory.ALBUMS,
            MediaIdCategory.PODCASTS_ALBUMS -> gateway.observeDetailAlbumSort()
            MediaIdCategory.ARTISTS,
            MediaIdCategory.PODCASTS_ARTISTS -> gateway.observeDetailArtistSort()
            MediaIdCategory.GENRES -> gateway.observeDetailGenreSort()
            else -> throw IllegalArgumentException("invalid media id $mediaId")
        }
    }

}