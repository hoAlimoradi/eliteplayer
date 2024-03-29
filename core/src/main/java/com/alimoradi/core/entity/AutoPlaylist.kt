package com.alimoradi.core.entity

import com.alimoradi.core.gateway.base.Id

enum class AutoPlaylist {
    LAST_ADDED,
    FAVORITE,
    HISTORY;

    companion object {
        @JvmStatic
        fun isAutoPlaylist(id: Id): Boolean {
            return values().find { it.id == id } != null
        }
    }

    val id: Long
        get() = this.hashCode().toLong()

}