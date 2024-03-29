@file:Suppress("NOTHING_TO_INLINE")

package com.alimoradi.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <T, R> Flow<List<T>>.mapListItem(crossinline mapper: (T) -> R): Flow<List<R>> {
    return this.map { it.map { mapper(it) } }
}
