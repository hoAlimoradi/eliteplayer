package com.alimoradi.core.interactor

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.gateway.podcast.PodcastAlbumGateway
import com.alimoradi.core.gateway.podcast.PodcastArtistGateway
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.*
import com.alimoradi.core.interactor.base.FlowUseCaseWithParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetItemTitleUseCase @Inject constructor(
    private val getFolderUseCase: FolderGateway,
    private val getPlaylistUseCase: PlaylistGateway,
    private val getSongUseCase: SongGateway,
    private val getAlbumUseCase: AlbumGateway,
    private val getArtistUseCase: ArtistGateway,
    private val getGenreUseCase: GenreGateway,

    private val getPodcastPlaylistUseCase: PodcastPlaylistGateway,
    private val getPodcastUseCase: PodcastGateway,
    private val getPodcastAlbumUseCase: PodcastAlbumGateway,
    private val getPodcastArtistUseCase: PodcastArtistGateway

) : FlowUseCaseWithParam<String, MediaId>() {


    override fun buildUseCase(param: MediaId): Flow<String> {
        return when (param.category){
            MediaIdCategory.FOLDERS -> getFolderUseCase.observeByParam(param.categoryValue).map { it?.title }
            MediaIdCategory.PLAYLISTS -> getPlaylistUseCase.observeByParam(param.categoryId).map { it?.title }
            MediaIdCategory.SONGS -> getSongUseCase.observeByParam(param.categoryId).map { it?.title }
            MediaIdCategory.ALBUMS -> getAlbumUseCase.observeByParam(param.categoryId).map { it?.title }
            MediaIdCategory.ARTISTS -> getArtistUseCase.observeByParam(param.categoryId).map { it?.name }
            MediaIdCategory.GENRES -> getGenreUseCase.observeByParam(param.categoryId).map { it?.name }
            MediaIdCategory.PODCASTS_PLAYLIST -> getPodcastPlaylistUseCase.observeByParam(param.categoryId).map { it?.title }
            MediaIdCategory.PODCASTS -> getPodcastUseCase.observeByParam(param.categoryId).map { it?.title }
            MediaIdCategory.PODCASTS_ARTISTS -> getPodcastArtistUseCase.observeByParam(param.categoryId).map { it?.name }
            MediaIdCategory.PODCASTS_ALBUMS -> getPodcastAlbumUseCase.observeByParam(param.categoryId).map { it?.title }
            else -> throw IllegalArgumentException("invalid media category ${param.category}")
        }.map { it ?: "" }
    }

}