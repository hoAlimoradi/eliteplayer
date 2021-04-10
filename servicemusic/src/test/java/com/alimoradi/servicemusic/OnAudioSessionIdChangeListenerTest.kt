package com.alimoradi.servicemusic

import androidx.lifecycle.Lifecycle
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.alimoradi.equalizer.bassboost.IBassBoost
import com.alimoradi.equalizer.equalizer.IEqualizer
import com.alimoradi.equalizer.virtualizer.IVirtualizer
import com.alimoradi.testshared.MainCoroutineRule
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class OnAudioSessionIdChangeListenerTest {

    @get:Rule
    var coroutinesMainDispatcherRule = MainCoroutineRule()

    private val lifecycle = mock<Lifecycle>()
    private val equalizer = mock<IEqualizer>()
    private val virtualizer = mock<IVirtualizer>()
    private val bassBoost = mock<IBassBoost>()

    private val sessionListener = OnAudioSessionIdChangeListener(
        lifecycle, equalizer, virtualizer, bassBoost
    )

    @Test
    fun `test lifecycle subscribe`() {
        verify(lifecycle).addObserver(sessionListener)
    }

    @Test
    fun `test on audio session id changed`() = runBlocking<Unit> {
        val latch = CountDownLatch(1)
        val audioSessionId = 1

        sessionListener.onAudioSessionId(audioSessionId)

        latch.await(OnAudioSessionIdChangeListener.DELAY, TimeUnit.MILLISECONDS)

        verify(equalizer).onAudioSessionIdChanged(1,audioSessionId)
        verify(virtualizer).onAudioSessionIdChanged(1,audioSessionId)
        verify(bassBoost).onAudioSessionIdChanged(1,audioSessionId)
    }

    @Test
    fun `test on audio session id changed very fast`() = runBlocking<Unit> {
        val latch = CountDownLatch(1)
        val audioSessionId1 = 1
        val audioSessionId2 = 2
        val audioSessionId3 = 3

        sessionListener.onAudioSessionId(audioSessionId1)
        sessionListener.onAudioSessionId(audioSessionId2)
        sessionListener.onAudioSessionId(audioSessionId3)

        latch.await(OnAudioSessionIdChangeListener.DELAY + 50, TimeUnit.MILLISECONDS)

        verify(equalizer).onAudioSessionIdChanged(1,audioSessionId3)
        verify(virtualizer).onAudioSessionIdChanged(1,audioSessionId3)
        verify(bassBoost).onAudioSessionIdChanged(1,audioSessionId3)
    }

    @Test
    fun `test release`() {
        sessionListener.release()

        verify(equalizer).onDestroy(1)
        verify(virtualizer).onDestroy(1)
        verify(bassBoost).onDestroy(1)
    }

}