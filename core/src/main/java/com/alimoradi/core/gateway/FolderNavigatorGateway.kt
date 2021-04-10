package com.alimoradi.core.gateway

import com.alimoradi.core.entity.FileType
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FolderNavigatorGateway {

    fun observeFolderChildren(file: File): Flow<List<FileType>>

}