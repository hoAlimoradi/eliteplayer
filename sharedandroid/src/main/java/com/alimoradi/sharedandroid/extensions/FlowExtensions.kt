package com.alimoradi.sharedandroid.extensions

import com.alimoradi.sharedandroid.utils.assertBackgroundThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

@Suppress("NOTHING_TO_INLINE")
inline fun <T> Flow<T>.assertBackground(): Flow<T> {
    return channelFlow {
        assertBackgroundThread()
        collect { offer(it) }
    }
}