package com.alimoradi.media.widget

import android.widget.ProgressBar
import com.alimoradi.intents.AppConstants
import com.alimoradi.sharedandroid.utils.isNougat
import com.alimoradi.shared.flowInterval
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

interface IProgressDeletegate {
    fun onStateChanged(state: com.alimoradi.media.model.PlayerPlaybackState)
    fun startAutoIncrement(startMillis: Int, speed: Float)
    fun stopAutoIncrement(startMillis: Int)
    fun observeProgress(): Flow<Long>
}

class ProgressDeletegate(
    private val progressBar: ProgressBar
) : IProgressDeletegate,
    CoroutineScope by MainScope() {

    private var incrementJob: Job? = null

    private val channel = ConflatedBroadcastChannel<Long>()

    override fun stopAutoIncrement(startMillis: Int) {
        incrementJob?.cancel()
        setProgress(progressBar, startMillis)
    }

    override fun startAutoIncrement(startMillis: Int, speed: Float) {
        stopAutoIncrement(startMillis)
        incrementJob = launch {
            flowInterval(
                AppConstants.PROGRESS_BAR_INTERVAL,
                TimeUnit.MILLISECONDS
            )
                .map { (it + 1) * AppConstants.PROGRESS_BAR_INTERVAL * speed + startMillis }
                .flowOn(Dispatchers.IO)
                .collect {
                    setProgress(progressBar, it.toInt())
                    channel.offer(it.toLong())
                }
        }
    }

    private fun setProgress(progressBar: ProgressBar, position: Int){
        progressBar.progress = position
    }

    override fun observeProgress(): Flow<Long> {
        return channel.asFlow()
    }

    override fun onStateChanged(state: com.alimoradi.media.model.PlayerPlaybackState) {
        if (state.isPlaying) {
            startAutoIncrement(state.bookmark, state.playbackSpeed)
        } else {
            stopAutoIncrement(state.bookmark)
        }
    }


}