package com.alimoradi.injection

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import dagger.BindsInstance
import dagger.Component
import com.alimoradi.analytics.AnalyticsModule
import com.alimoradi.analytics.TrackerFacade
import com.alimoradi.core.IEncrypter
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.gateway.*
import com.alimoradi.core.gateway.podcast.PodcastAlbumGateway
import com.alimoradi.core.gateway.podcast.PodcastArtistGateway
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.podcast.PodcastPlaylistGateway
import com.alimoradi.core.gateway.track.*
import com.alimoradi.core.prefs.*
import com.alimoradi.data.DataModule
import com.alimoradi.data.PreferenceModule
import com.alimoradi.data.RepositoryHelperModule
import com.alimoradi.data.api.lastfm.NetworkModule
import com.alimoradi.equalizer.EqualizerModule
import com.alimoradi.equalizer.bassboost.IBassBoost
import com.alimoradi.equalizer.equalizer.IEqualizer
import com.alimoradi.equalizer.virtualizer.IVirtualizer
import com.alimoradi.injection.schedulers.SchedulersModule
import javax.inject.Singleton

@Component(
    modules = arrayOf(
        CoreModule::class,
        SchedulersModule::class,
        NetworkModule::class,
        AnalyticsModule::class,

//        // data
        RepositoryHelperModule::class,
        PreferenceModule::class,
        DataModule::class,
        EqualizerModule::class
    )
)
@Singleton
interface CoreComponent {

    @ApplicationContext
    fun context(): Context
    fun resources(): Resources

    fun lastFmGateway(): ImageRetrieverGateway

    fun prefs(): AppPreferencesGateway
    fun musicPrefs(): MusicPreferencesGateway
    fun tutorialPrefs(): TutorialPreferenceGateway
    fun equalizerPrefs(): EqualizerPreferencesGateway
    fun sortPrefs(): SortPreferences
    fun blacklistPrefs(): BlacklistPreferences
    
    fun playingQueueGateway(): PlayingQueueGateway
    fun favoriteGateway(): FavoriteGateway
    fun recentSearches(): RecentSearchesGateway
    fun offlineLyrics(): OfflineLyricsGateway

    fun sharedPreferences(): SharedPreferences

    fun equalizer(): IEqualizer
    fun virtualizer(): IVirtualizer
    fun bassBoost(): IBassBoost

    fun folderGateway(): FolderGateway
    fun folderNavigatorGateway(): FolderNavigatorGateway
    fun playlistGateway(): PlaylistGateway
    fun songGateway(): SongGateway
    fun albumGateway(): AlbumGateway
    fun artistGateway(): ArtistGateway
    fun genreGateway(): GenreGateway
    fun podcastPlaylistGateway(): PodcastPlaylistGateway
    fun podcastGateway(): PodcastGateway
    fun podcastAlbumGateway(): PodcastAlbumGateway
    fun podcastArtistGateway(): PodcastArtistGateway

    fun equalizerGateway(): EqualizerGateway

    fun encrypter(): IEncrypter

    fun trackerFacade(): TrackerFacade

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance instance: Application): CoreComponent
    }

    companion object {
        private var component: CoreComponent? = null

        @JvmStatic
        fun coreComponent(application: Application): CoreComponent {
            if (component == null) {
                component = DaggerCoreComponent.factory().create(application)
            }
            return component!!
        }
    }

}