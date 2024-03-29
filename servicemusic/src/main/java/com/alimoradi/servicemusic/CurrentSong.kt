package com.alimoradi.servicemusic

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.LastMetadata
import com.alimoradi.core.entity.favorite.FavoriteEnum
import com.alimoradi.core.entity.favorite.FavoriteStateEntity
import com.alimoradi.core.entity.favorite.FavoriteType
import com.alimoradi.core.interactor.*
import com.alimoradi.core.interactor.favorite.IsFavoriteSongUseCase
import com.alimoradi.core.interactor.favorite.UpdateFavoriteStateUseCase
import com.alimoradi.core.interactor.lastplayed.InsertLastPlayedAlbumUseCase
import com.alimoradi.core.interactor.lastplayed.InsertLastPlayedArtistUseCase
import com.alimoradi.core.prefs.MusicPreferencesGateway
import com.alimoradi.injection.dagger.PerService
import com.alimoradi.servicemusic.interfaces.IPlayerLifecycle
import com.alimoradi.servicemusic.model.MediaEntity
import com.alimoradi.servicemusic.model.MetadataEntity
import com.alimoradi.shared.CustomScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@PerService
internal class CurrentSong @Inject constructor(
    insertMostPlayedUseCase: InsertMostPlayedUseCase,
    insertHistorySongUseCase: InsertHistorySongUseCase,
    private val musicPreferencesUseCase: MusicPreferencesGateway,
    private val isFavoriteSongUseCase: IsFavoriteSongUseCase,
    private val updateFavoriteStateUseCase: UpdateFavoriteStateUseCase,
    insertLastPlayedAlbumUseCase: InsertLastPlayedAlbumUseCase,
    insertLastPlayedArtistUseCase: InsertLastPlayedArtistUseCase,
    playerLifecycle: IPlayerLifecycle

) : DefaultLifecycleObserver,
    CoroutineScope by CustomScope(),
    IPlayerLifecycle.Listener {

    companion object {
        @JvmStatic
        private val TAG = "SM:${CurrentSong::class.java.simpleName}"
    }

    private var isFavoriteJob: Job? = null

    private val channel = Channel<MediaEntity>(Channel.UNLIMITED)

    init {
        playerLifecycle.addListener(this)

        launch(Dispatchers.IO) {
            for (entity in channel) {
                Log.v(TAG, "on new item ${entity.title}")
                if (entity.mediaId.isArtist || entity.mediaId.isPodcastArtist) {
                    Log.v(TAG, "insert last played artist ${entity.title}")
                    insertLastPlayedArtistUseCase(entity.mediaId)
                } else if (entity.mediaId.isAlbum || entity.mediaId.isPodcastAlbum) {
                    Log.v(TAG, "insert last played album ${entity.title}")
                    insertLastPlayedAlbumUseCase(entity.mediaId)
                }

                Log.v(TAG, "insert most played ${entity.title}")
                MediaId.playableItem(entity.mediaId, entity.id)
                insertMostPlayedUseCase(entity.mediaId)

                Log.v(TAG, "insert to history ${entity.title}")
                insertHistorySongUseCase(
                    InsertHistorySongUseCase.Input(
                        entity.id,
                        entity.isPodcast
                    )
                )
            }
        }

    }

    override fun onDestroy(owner: LifecycleOwner) {
        isFavoriteJob?.cancel()
        cancel()
    }

    override fun onPrepare(metadata: MetadataEntity) {
        updateFavorite(metadata.entity)
        saveLastMetadata(metadata.entity)
    }

    override fun onMetadataChanged(metadata: MetadataEntity) {
        channel.offer(metadata.entity)
        updateFavorite(metadata.entity)
        saveLastMetadata(metadata.entity)
    }

    private fun updateFavorite(mediaEntity: MediaEntity) {
        Log.v(TAG, "updateFavorite ${mediaEntity.title}")

        isFavoriteJob?.cancel()
        isFavoriteJob = launch {
            val type = if (mediaEntity.isPodcast) FavoriteType.PODCAST else FavoriteType.TRACK
            val isFavorite =
                isFavoriteSongUseCase(IsFavoriteSongUseCase.Input(mediaEntity.id, type))
            val isFavoriteEnum =
                if (isFavorite) FavoriteEnum.FAVORITE else FavoriteEnum.NOT_FAVORITE
            updateFavoriteStateUseCase(FavoriteStateEntity(mediaEntity.id, isFavoriteEnum, type))
        }
    }

    private fun saveLastMetadata(entity: MediaEntity) {
        Log.v(TAG, "saveLastMetadata ${entity.title}")
        launch {
            musicPreferencesUseCase.setLastMetadata(
                LastMetadata(
                    entity.title, entity.artist, entity.id
                )
            )
        }
    }

}