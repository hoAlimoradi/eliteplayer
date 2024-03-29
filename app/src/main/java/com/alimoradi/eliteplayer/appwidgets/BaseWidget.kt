package com.alimoradi.eliteplayer.appwidgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.alimoradi.eliteplayer.R
import com.alimoradi.core.entity.LastMetadata
import com.alimoradi.core.prefs.MusicPreferencesGateway
import com.alimoradi.servicemusic.MusicService
import com.alimoradi.presentation.main.MainActivity
import com.alimoradi.sharedandroid.palette.ImageProcessorResult
import com.alimoradi.sharedandroid.extensions.asServicePendingIntent
import com.alimoradi.sharedandroid.extensions.getAppWidgetsIdsFor
import com.alimoradi.intents.AppConstants
import com.alimoradi.intents.Classes
import com.alimoradi.intents.MusicServiceAction
import javax.inject.Inject

abstract class BaseWidget : AbsWidgetApp() {

    companion object {
        @JvmStatic
        private var IS_PLAYING = false
    }

    @Inject lateinit var musicPrefsUseCase: MusicPreferencesGateway

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == "mobi.intuitit.android.hpp.ACTION_READY"){
            val appWidgetManager = context.getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
            for (clazz in Classes.widgets) {
                val ids = context.getAppWidgetsIdsFor(clazz)
                onUpdate(context, appWidgetManager, ids)
            }
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val remoteViews = RemoteViews(context.packageName, layoutId)

        val playPauseIcon = if (IS_PLAYING){
            ContextCompat.getDrawable(context, R.drawable.vd_pause_big)!!
        } else ContextCompat.getDrawable(context, R.drawable.vd_play_big)!!

        remoteViews.setImageViewBitmap(R.id.play, playPauseIcon.toBitmap())

        remoteViews.setOnClickPendingIntent(R.id.previous, buildPendingIntent(context, MusicServiceAction.SKIP_PREVIOUS.name))
        remoteViews.setOnClickPendingIntent(R.id.play, buildPendingIntent(context, MusicServiceAction.PLAY_PAUSE.name))
        remoteViews.setOnClickPendingIntent(R.id.next, buildPendingIntent(context, MusicServiceAction.SKIP_NEXT.name))
        remoteViews.setOnClickPendingIntent(R.id.cover, buildContentIntent(context))

        val metadata = musicPrefsUseCase.getLastMetadata().safeMap(context)
        onMetadataChanged(context, metadata.toWidgetMetadata(), appWidgetIds, remoteViews)
    }

    override fun onPlaybackStateChanged(context: Context, state: WidgetState, appWidgetIds: IntArray) {
        IS_PLAYING = state.isPlaying

        val remoteViews = RemoteViews(context.packageName, layoutId)

        val playPauseIcon = if (state.isPlaying){
            ContextCompat.getDrawable(context, R.drawable.vd_pause_big)!!
        } else ContextCompat.getDrawable(context, R.drawable.vd_play_big)!!

        remoteViews.setImageViewBitmap(R.id.play, playPauseIcon.toBitmap())

        remoteViews.setOnClickPendingIntent(R.id.previous, buildPendingIntent(context, MusicServiceAction.SKIP_PREVIOUS.name))
        remoteViews.setOnClickPendingIntent(R.id.play, buildPendingIntent(context, MusicServiceAction.PLAY_PAUSE.name))
        remoteViews.setOnClickPendingIntent(R.id.next, buildPendingIntent(context, MusicServiceAction.SKIP_NEXT.name))
        remoteViews.setOnClickPendingIntent(R.id.cover, buildContentIntent(context))

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetIds, remoteViews)
    }

    override fun onActionVisibilityChanged(context: Context, actions: WidgetActions, appWidgetIds: IntArray) {
        val showPrevious = actions.showPrevious
        val showNext = actions.showNext

        val remoteViews = RemoteViews(context.packageName, layoutId)

        val previousVisibility = if (showPrevious) View.VISIBLE else View.INVISIBLE
        val nextVisibility = if (showNext) View.VISIBLE else View.INVISIBLE

        val previousPendingIntent = if (showPrevious) buildPendingIntent(context, MusicServiceAction.SKIP_PREVIOUS.name)
            else null
        val nextPendingIntent = if (showNext) buildPendingIntent(context, MusicServiceAction.SKIP_NEXT.name)
            else null

        remoteViews.setViewVisibility(R.id.previous, previousVisibility)
        remoteViews.setViewVisibility(R.id.next, nextVisibility)
        remoteViews.setOnClickPendingIntent(R.id.previous, previousPendingIntent)
        remoteViews.setOnClickPendingIntent(R.id.next, nextPendingIntent)

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetIds, remoteViews)
    }

    private fun buildPendingIntent(context: Context, action: String): PendingIntent? {
        val intent = Intent(context, MusicService::class.java)
        intent.action = action
        return intent.asServicePendingIntent(context, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildContentIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.action = AppConstants.ACTION_CONTENT_VIEW
        return PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    protected fun setMediaButtonColors(remoteViews: RemoteViews, color: Int){
        remoteViews.setInt(R.id.previous, "setColorFilter", color)
        remoteViews.setInt(R.id.play, "setColorFilter", color)
        remoteViews.setInt(R.id.next, "setColorFilter", color)
    }

    protected fun updateTextColor(remoteViews: RemoteViews, palette: ImageProcessorResult){
        remoteViews.setTextColor(R.id.title, palette.primaryTextColor)
        remoteViews.setTextColor(R.id.subtitle, palette.secondaryTextColor)
    }

    protected abstract val layoutId : Int

    override fun onSizeChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, size: WidgetSize) {
        val remoteViews = RemoteViews(context.packageName, layoutId)

        if (size.minHeight > 100){
            remoteViews.setInt(R.id.title, "setMaxLines", Int.MAX_VALUE)
            remoteViews.setInt(R.id.subtitle, "setMaxLines", 2)

        } else {
            remoteViews.setInt(R.id.title, "setMaxLines", 1)
            remoteViews.setInt(R.id.subtitle, "setMaxLines", 1)
        }
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteViews)
    }

    private fun LastMetadata.safeMap(context: Context): LastMetadata {
        val title = if (this.title.isBlank()) context.getString(R.string.common_placeholder_title) else this.title
        val subtitle = if (this.subtitle.isBlank()) context.getString(R.string.common_placeholder_artist) else this.subtitle

        return LastMetadata(
            title,
            subtitle,
            this.id
        )
    }

    private fun LastMetadata.toWidgetMetadata(): WidgetMetadata {
        return WidgetMetadata(
            this.id,
            this.title,
            this.subtitle
        )
    }

}