package com.alimoradi.core.gateway

import com.alimoradi.core.entity.PlayingQueueSong
import com.alimoradi.core.interactor.UpdatePlayingQueueUseCaseRequest
import kotlinx.coroutines.flow.Flow

interface PlayingQueueGateway {

    companion object {
        const val MINI_QUEUE_SIZE = 50
    }

    fun observeAll(): Flow<List<PlayingQueueSong>>

    fun getAll(): List<PlayingQueueSong>

    fun update(list: List<UpdatePlayingQueueUseCaseRequest>)

}