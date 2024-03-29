package com.alimoradi.presentation.tree

import android.content.Context
import android.database.CursorIndexOutOfBoundsException
import android.os.Environment
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.entity.FileType
import com.alimoradi.core.gateway.FolderNavigatorGateway
import com.alimoradi.core.prefs.AppPreferencesGateway
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.DisplayableFile
import com.alimoradi.sharedandroid.extensions.asLiveData
import com.alimoradi.sharedandroid.extensions.distinctUntilChanged
import com.alimoradi.shared.startWithIfNotEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class FolderTreeFragmentViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appPreferencesUseCase: AppPreferencesGateway,
    private val gateway: FolderNavigatorGateway

) : ViewModel() {

    companion object {
        @JvmStatic
        val BACK_HEADER_ID = MediaId.headerId("back header")
    }

    private val currentDirectory: ConflatedBroadcastChannel<File> =
        ConflatedBroadcastChannel(appPreferencesUseCase.getDefaultMusicFolder())

    private val isCurrentFolderDefaultFolder = MutableLiveData<Boolean>()

    private val currentDirectoryChildrenLiveData = MutableLiveData<List<DisplayableFile>>()

    init {
        viewModelScope.launch {
            currentDirectory.asFlow()
                .flatMapLatest { file ->
                    gateway.observeFolderChildren(file)
                        .map { addHeaders(file, it) }
                }
                .flowOn(Dispatchers.Default)
                .collect {
                    currentDirectoryChildrenLiveData.value = it
                }
        }
        viewModelScope.launch {
            currentDirectory.asFlow().combine(appPreferencesUseCase.observeDefaultMusicFolder())
            { current, default -> current.path == default.path }
                .collect { isCurrentFolderDefaultFolder.value = it }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }

    private fun addHeaders(parent: File, files: List<FileType>): List<DisplayableFile> {
        val folders = files.asSequence()
            .filterIsInstance(FileType.Folder::class.java)
            .map { it.toDisplayableItem() }
            .toList()
            .startWithIfNotEmpty(foldersHeader)

        val tracks = files.asSequence()
            .filterIsInstance(FileType.Track::class.java)
            .map { it.toDisplayableItem() }
            .toList()
            .startWithIfNotEmpty(tracksHeader)

        if (parent == Environment.getRootDirectory()) {
            return folders + tracks
        }
        return backDisplayableItem + folders + tracks
    }

    fun observeChildren(): LiveData<List<DisplayableFile>> = currentDirectoryChildrenLiveData
    fun observeCurrentDirectoryFileName(): LiveData<File> = currentDirectory.asFlow().asLiveData()
    fun observeCurrentFolderIsDefaultFolder(): LiveData<Boolean> = isCurrentFolderDefaultFolder.distinctUntilChanged()

    fun popFolder(): Boolean {
        val current = currentDirectory.value
        if (current == Environment.getRootDirectory()) {
            // alredy in root dir
            return false
        }

        val parent = current.parentFile
        if (parent?.listFiles()?.isEmpty() == true) {
            // parent has not children
            return false
        }

        try {
            currentDirectory.offer(current.parentFile!!)
            return true
        } catch (e: Throwable) {
            e.printStackTrace()
            return false
        }
    }

    fun nextFolder(file: File) {
        require(file.isDirectory)
        currentDirectory.offer(file)
    }

    fun updateDefaultFolder() {
        val currentFolder = currentDirectory.value
        require(currentFolder.isDirectory)
        appPreferencesUseCase.setDefaultMusicFolder(currentFolder)
    }

    @Suppress("DEPRECATION")
    fun createMediaId(item: DisplayableFile): MediaId? {
        try {
            val file = item.asFile()
            val songPath = file.path
            val path = songPath.substring(0, songPath.lastIndexOf(File.separator))
            val folderMediaId = MediaId.createCategoryValue(MediaIdCategory.FOLDERS, path)

            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(BaseColumns._ID),
                "${MediaStore.Audio.AudioColumns.DATA} = ?",
                arrayOf(file.path), null
            )?.use { cursor ->
                cursor.moveToFirst()
                val trackId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                return MediaId.playableItem(folderMediaId, trackId)
            }
        } catch (ex: CursorIndexOutOfBoundsException) {
            ex.printStackTrace()
        }
        return null
    }

    private val backDisplayableItem: List<DisplayableFile> = listOf(
        DisplayableFile(
            R.layout.item_folder_tree_directory,
            BACK_HEADER_ID,
            "...",
            null
        )
    )

    private val foldersHeader = DisplayableFile(
        R.layout.item_folder_tree_header,
        MediaId.headerId("folder header"),
        context.getString(R.string.common_folders),
        null
    )

    private val tracksHeader = DisplayableFile(
        R.layout.item_folder_tree_header,
        MediaId.headerId("track header"),
        context.getString(R.string.common_tracks),
        null
    )

    private fun FileType.Track.toDisplayableItem(): DisplayableFile {

        return DisplayableFile(
            type = R.layout.item_folder_tree_track,
            mediaId = MediaId.createCategoryValue(MediaIdCategory.FOLDERS, this.path),
            title = this.title,
            path = this.path
        )
    }

    private fun FileType.Folder.toDisplayableItem(): DisplayableFile {

        return DisplayableFile(
            type = R.layout.item_folder_tree_directory,
            mediaId = MediaId.createCategoryValue(MediaIdCategory.FOLDERS, this.path),
            title = this.name,
            path = this.path
        )
    }
}