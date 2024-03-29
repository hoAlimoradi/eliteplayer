package com.alimoradi.servicemusic.state

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.request.target.Target
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.prefs.MusicPreferencesGateway
import com.alimoradi.imageprovider.GlideUtils
import com.alimoradi.imageprovider.getCachedBitmap
import com.alimoradi.injection.dagger.PerService
import com.alimoradi.servicemusic.interfaces.IPlayerLifecycle
import com.alimoradi.servicemusic.model.MediaEntity
import com.alimoradi.servicemusic.model.MetadataEntity
import com.alimoradi.servicemusic.model.SkipType
import com.alimoradi.intents.Classes
import com.alimoradi.intents.MusicConstants
import com.alimoradi.intents.WidgetConstants
import com.alimoradi.servicemusic.utils.putBoolean
import com.alimoradi.shared.CustomScope
import com.alimoradi.sharedandroid.extensions.getAppWidgetsIdsFor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@PerService
internal class MusicServiceMetadata @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mediaSession: MediaSessionCompat,
    playerLifecycle: IPlayerLifecycle,
    private val musicPrefs: MusicPreferencesGateway

) : IPlayerLifecycle.Listener,
    DefaultLifecycleObserver,
    CoroutineScope by CustomScope() {

    companion object {
        @JvmStatic
        private val TAG = "SM:${MusicServiceMetadata::class.java.simpleName}"
    }

    private val builder = MediaMetadataCompat.Builder()

    private var showLockScreenArtwork = false

    init {
        playerLifecycle.addListener(this)

        launch {
            musicPrefs.observeShowLockscreenArtwork()
                .collect { showLockScreenArtwork = it }
        }
    }

    override fun onPrepare(metadata: MetadataEntity) {
        onMetadataChanged(metadata)
    }

    override fun onMetadataChanged(metadata: MetadataEntity) {
        update(metadata)
        notifyWidgets(metadata.entity)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        cancel()
    }

    private fun update(metadata: MetadataEntity) {
        Log.v(TAG, "update metadata ${metadata.entity.title}, skip type=${metadata.skipType}")

        launch {

            val entity = metadata.entity

            builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, entity.mediaId.toString())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, entity.title)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, entity.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, entity.album)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, entity.title)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, entity.artist)
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, entity.album)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, entity.duration)
                .putString(MusicConstants.PATH, entity.path)
                .putBoolean(MusicConstants.IS_PODCAST, entity.isPodcast)
                .putBoolean(MusicConstants.SKIP_NEXT, metadata.skipType == SkipType.SKIP_NEXT)
                .putBoolean(MusicConstants.SKIP_PREVIOUS, metadata.skipType == SkipType.SKIP_PREVIOUS)

            yield()

            if (showLockScreenArtwork) {
                val bitmap = context.getCachedBitmap(entity.mediaId, GlideUtils.OVERRIDE_BIG)
                yield()
                builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
            }
            mediaSession.setMetadata(builder.build())
        }
    }

    private fun notifyWidgets(entity: MediaEntity) {
        Log.v(TAG, "notify widgets ${entity.title}")

        for (clazz in Classes.widgets) {
            val ids = context.getAppWidgetsIdsFor(clazz)

            val intent = Intent(context, clazz).apply {
                action = WidgetConstants.METADATA_CHANGED
                putExtra(WidgetConstants.ARGUMENT_SONG_ID, entity.id)
                putExtra(WidgetConstants.ARGUMENT_TITLE, entity.title)
                putExtra(WidgetConstants.ARGUMENT_SUBTITLE, entity.artist)
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }

            context.sendBroadcast(intent)
        }

    }

}
