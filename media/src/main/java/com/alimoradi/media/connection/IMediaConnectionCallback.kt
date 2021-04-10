package com.alimoradi.media.connection

internal interface IMediaConnectionCallback {
    fun onConnectionStateChanged(state: MusicServiceConnectionState)
}