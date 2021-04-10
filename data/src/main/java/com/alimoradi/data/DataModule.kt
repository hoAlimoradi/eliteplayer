package com.alimoradi.data

import dagger.Binds
import dagger.Module
import com.alimoradi.core.gateway.*
import com.alimoradi.core.gateway.podcast.PodcastAlbumGateway
import com.alimoradi.core.gateway.podcast.PodcastArtistGateway
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.*
import com.alimoradi.data.repository.*
import com.alimoradi.data.repository.lastfm.ImageRetrieverRepository
import com.alimoradi.data.repository.podcast.PodcastAlbumRepository
import com.alimoradi.data.repository.podcast.PodcastArtistRepository
import com.alimoradi.data.repository.podcast.PodcastPlaylistRepository
import com.alimoradi.data.repository.podcast.PodcastRepository
import com.alimoradi.data.repository.track.*
import javax.inject.Singleton

@Module
abstract class DataModule {


    // tracks

    @Binds
    @Singleton
    internal abstract fun provideFolderRepository(repository: FolderRepository): FolderGateway

    @Binds
    @Singleton
    internal abstract fun providePlaylistRepository(repository: PlaylistRepository): PlaylistGateway

    @Binds
    @Singleton
    internal abstract fun provideSongRepository(repository: SongRepository): SongGateway

    @Binds
    @Singleton
    internal abstract fun provideAlbumRepository(repository: AlbumRepository): AlbumGateway

    @Binds
    @Singleton
    internal abstract fun provideArtistRepository(repository: ArtistRepository): ArtistGateway

    @Binds
    @Singleton
    internal abstract fun provideGenreRepository(repository: GenreRepository): GenreGateway

    // podcasts
    @Binds
    @Singleton
    internal abstract fun providePodcastPlaylistRepository(repository: PodcastPlaylistRepository): PodcastPlaylistGateway

    @Binds
    @Singleton
    internal abstract fun providePodcsatRepository(repository: PodcastRepository): PodcastGateway
    @Binds
    @Singleton
    internal abstract fun providePodcastAlbumRepository(repository: PodcastAlbumRepository): PodcastAlbumGateway
    @Binds
    @Singleton
    internal abstract fun providePodcastArtistRepository(repository: PodcastArtistRepository): PodcastArtistGateway

    // other

    @Binds
    @Singleton
    internal abstract fun provideLastFmRepository(repository: ImageRetrieverRepository): ImageRetrieverGateway

    @Binds
    @Singleton
    internal abstract fun providePlayingQueueRepository(repository: PlayingQueueRepository): PlayingQueueGateway

    @Binds
    @Singleton
    internal abstract fun provideFavoriteRepository(repository: FavoriteRepository): FavoriteGateway

    @Binds
    @Singleton
    internal abstract fun provideRecentSearchesRepository(repository: RecentSearchesRepository): RecentSearchesGateway

    @Binds
    @Singleton
    internal abstract fun provideLyricsRepository(repository: OfflineLyricsRepository): OfflineLyricsGateway

    @Binds
    @Singleton
    internal abstract fun provideFolderNavigator(repository: FolderNavigatorRepository): FolderNavigatorGateway

    @Binds
    @Singleton
    internal abstract fun provideEqualzierRepository(repository: EqualizerRepository): EqualizerGateway

}