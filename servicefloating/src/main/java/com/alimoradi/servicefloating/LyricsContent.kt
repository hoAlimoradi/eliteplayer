package com.alimoradi.servicefloating

import android.content.Context
import androidx.lifecycle.Lifecycle
import com.alimoradi.media.model.PlayerState
import com.alimoradi.sharedandroid.extensions.distinctUntilChanged
import com.alimoradi.sharedandroid.extensions.filter
import com.alimoradi.sharedandroid.extensions.map
import com.alimoradi.sharedandroid.extensions.subscribe
import kotlinx.android.synthetic.main.content_offline_lyrics.view.*
import kotlinx.android.synthetic.main.content_web_view_with_player.view.*

class LyricsContent(
    lifecycle: Lifecycle,
    context: Context,
    private val glueService: MusicGlueService

) : WebViewContent(lifecycle, context, R.layout.content_web_view_with_player) {

    override fun onShown() {
        super.onShown()

        glueService.observePlaybackState()
            .subscribe(this) {
                content.seekBar.onStateChanged(it)
            }

        glueService.observePlaybackState()
            .filter { it.isPlayOrPause }
            .map { it.state }
            .distinctUntilChanged()
            .subscribe(this) {
                when (it){
                    PlayerState.PLAYING -> content.playPause.animationPlay(true)
                    PlayerState.PAUSED -> content.playPause.animationPause(true)
                    else -> throw IllegalArgumentException("state not valid $it")
                }
            }

        glueService.observeMetadata()
            .subscribe(this) {
                content.header.text = it.title
                content.subHeader.text = it.artist
            }

        glueService.observeMetadata()
            .subscribe(this) {
                content.seekBar.max = it.duration.toInt()
            }

        content.playPause.setOnClickListener { glueService.playPause() }

        content.seekBar.setListener(onProgressChanged = {}, onStartTouch = {}, onStopTouch = {
            glueService.seekTo(content.seekBar.progress.toLong())
        })
    }

    override fun onHidden() {
        super.onHidden()
        content.playPause.setOnClickListener(null)
        content.seekBar.setOnSeekBarChangeListener(null)
    }

    override fun getUrl(item: String): String {
        return "http://www.google.it/search?q=$item+lyrics"
    }
}