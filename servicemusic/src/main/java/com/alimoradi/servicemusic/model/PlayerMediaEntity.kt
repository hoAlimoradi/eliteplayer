package com.alimoradi.servicemusic.model

internal data class PlayerMediaEntity(
    val mediaEntity: MediaEntity,
    val positionInQueue: PositionInQueue,
    val bookmark: Long
)