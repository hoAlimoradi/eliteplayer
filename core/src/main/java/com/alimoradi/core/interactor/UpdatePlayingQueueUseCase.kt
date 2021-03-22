package com.alimoradi.core.interactor

import com.alimoradi.core.MediaId
import com.alimoradi.core.gateway.PlayingQueueGateway
import javax.inject.Inject

class UpdatePlayingQueueUseCase @Inject constructor(
    private val gateway: PlayingQueueGateway
) {

    operator fun invoke(param: List<UpdatePlayingQueueUseCaseRequest>) {
        gateway.update(param)
    }

}

data class UpdatePlayingQueueUseCaseRequest(
    val mediaId: MediaId,
    val songId: Long,
    val idInPlaylist: Int
)