package com.alimoradi.servicemusic.interfaces

import com.alimoradi.servicemusic.model.PlayerMediaEntity
import com.alimoradi.servicemusic.model.SkipType

internal interface IPlayer : IPlayerLifecycle {

    fun prepare(playerModel: PlayerMediaEntity)
    fun playNext(playerModel: PlayerMediaEntity, skipType: SkipType)
    fun play(playerModel: PlayerMediaEntity)

    fun seekTo(millis: Long)

    fun resume()
    fun pause(stopService: Boolean, releaseFocus: Boolean = true)

    fun isPlaying(): Boolean
    fun getBookmark(): Long

    fun forwardTenSeconds()
    fun replayTenSeconds()

    fun forwardThirtySeconds()
    fun replayThirtySeconds()

    fun stopService()

    fun setVolume(volume: Float)
}

