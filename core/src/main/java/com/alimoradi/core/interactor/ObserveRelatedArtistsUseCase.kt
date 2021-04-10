package com.alimoradi.core.interactor

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.entity.track.Artist
import com.alimoradi.core.gateway.track.FolderGateway
import com.alimoradi.core.gateway.track.GenreGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.interactor.base.FlowUseCaseWithParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveRelatedArtistsUseCase @Inject constructor(
    private val folderGateway: FolderGateway,
    private val playlistGateway: PlaylistGateway,
    private val genreGateway: GenreGateway,
    private val podcastPlaylistGateway: PodcastPlaylistGateway

) : FlowUseCaseWithParam<List<Artist>, MediaId>() {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun buildUseCase(mediaId: MediaId): Flow<List<Artist>> {
        return when (mediaId.category) {
            MediaIdCategory.FOLDERS -> folderGateway.observeRelatedArtists(mediaId.categoryValue)
            MediaIdCategory.PLAYLISTS -> playlistGateway.observeRelatedArtists(mediaId.categoryId)
            MediaIdCategory.GENRES -> genreGateway.observeRelatedArtists(mediaId.categoryId)
            MediaIdCategory.PODCASTS_PLAYLIST -> podcastPlaylistGateway.observeRelatedArtists(mediaId.categoryId)
            else -> flowOf(listOf())
        }
    }
}