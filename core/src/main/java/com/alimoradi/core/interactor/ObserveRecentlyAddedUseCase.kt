package com.alimoradi.core.interactor

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.track.FolderGateway
import com.alimoradi.core.gateway.track.GenreGateway
import com.alimoradi.core.interactor.base.FlowUseCaseWithParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveRecentlyAddedUseCase @Inject constructor(
    private val folderGateway: FolderGateway,
    private val genreGateway: GenreGateway

) : FlowUseCaseWithParam<List<Song>, MediaId>() {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun buildUseCase(mediaId: MediaId): Flow<List<Song>> {
        return when (mediaId.category){
            MediaIdCategory.FOLDERS -> folderGateway.observeRecentlyAdded(mediaId.categoryValue)
            MediaIdCategory.GENRES -> genreGateway.observeRecentlyAdded(mediaId.categoryId)
            else -> flowOf(listOf())
        }
    }
}