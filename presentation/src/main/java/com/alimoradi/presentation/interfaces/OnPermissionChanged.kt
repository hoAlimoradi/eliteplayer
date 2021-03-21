package com.alimoradi.presentation.interfaces

interface OnPermissionChanged {
    fun onPermissionGranted(permission: Permission)
}

enum class Permission {
    STORAGE
}