package com.alimoradi.servicemusic

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.alimoradi.servicemusic.interfaces.IPlayerLifecycle
import com.alimoradi.servicemusic.model.MetadataEntity
import com.alimoradi.servicemusic.model.SkipType
import com.alimoradi.servicemusic.shared.MusicServiceData
import com.alimoradi.servicemusic.state.MusicServiceMetadata
import com.alimoradi.testshared.MainCoroutineRule
import org.junit.Rule
import org.junit.Test

class MusicServiceMetadataTest {

    @get:Rule
    var coroutinesMainDispatcherRule = MainCoroutineRule()

    private val context = mock<Context>()
    private val mediaSession = mock<MediaSessionCompat>()
    private val playerLifecycle = mock<IPlayerLifecycle>()

    private val musicServiceMetadata = MusicServiceMetadata(
        context, mediaSession, playerLifecycle, mock()
    )

    @Test
    fun `test subscription`() {
        verify(playerLifecycle).addListener(musicServiceMetadata)
    }

    @Test
    fun `test onPrepared`() {
        val item = MusicServiceData.mediaEntity
        val metadataItem = MetadataEntity(item, SkipType.NONE)

        val spy = spy(musicServiceMetadata)

        spy.onPrepare(metadataItem)

        verify(spy).onMetadataChanged(metadataItem)
    }

}