package com.alimoradi.core.interactor

import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.gateway.track.FolderGateway
import com.alimoradi.core.gateway.track.GenreGateway
import com.alimoradi.core.gateway.track.PlaylistGateway
import javax.inject.Inject

class InsertMostPlayedUseCase @Inject constructor(
    private val folderGateway: FolderGateway,
    private val playlistGateway: PlaylistGateway,
    private val genreGateway: GenreGateway

) {

    suspend operator fun invoke(mediaId: MediaId) {
        when (mediaId.category) {
            MediaIdCategory.FOLDERS -> folderGateway.insertMostPlayed(mediaId)
            MediaIdCategory.PLAYLISTS -> playlistGateway.insertMostPlayed(mediaId)
            MediaIdCategory.GENRES -> genreGateway.insertMostPlayed(mediaId)
            else -> return
        }
    }

}