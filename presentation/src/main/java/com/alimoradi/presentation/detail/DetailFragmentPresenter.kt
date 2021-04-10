package com.alimoradi.presentation.detail

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.AutoPlaylist
import com.alimoradi.core.entity.PlaylistType
import com.alimoradi.core.interactor.playlist.MoveItemInPlaylistUseCase
import com.alimoradi.core.interactor.playlist.RemoveFromPlaylistUseCase
import com.alimoradi.core.prefs.TutorialPreferenceGateway
import com.alimoradi.presentation.model.DisplayableTrack
import javax.inject.Inject

class DetailFragmentPresenter @Inject constructor(
    private val mediaId: MediaId,
    private val removeFromPlaylistUseCase: RemoveFromPlaylistUseCase,
    private val moveItemInPlaylistUseCase: MoveItemInPlaylistUseCase,
    private val tutorialPreferenceUseCase: TutorialPreferenceGateway

) {

    suspend fun removeFromPlaylist(item: DisplayableTrack) {
        mediaId.assertPlaylist()
        val playlistId = mediaId.categoryId
        val playlistType = if (item.mediaId.isPodcastPlaylist) PlaylistType.PODCAST else PlaylistType.TRACK
        if (playlistId == AutoPlaylist.FAVORITE.id){
            // favorites use songId instead of idInPlaylist
            removeFromPlaylistUseCase(
                RemoveFromPlaylistUseCase.Input(
                    playlistId, item.mediaId.leaf!!, playlistType
            ))
        } else {
            removeFromPlaylistUseCase(
                RemoveFromPlaylistUseCase.Input(
                playlistId, item.idInPlaylist.toLong(), playlistType
            ))
        }
    }

    suspend fun moveInPlaylist(moveList: List<Pair<Int, Int>>){
        mediaId.assertPlaylist()
        val playlistId = mediaId.resolveId
        moveItemInPlaylistUseCase.execute(
            MoveItemInPlaylistUseCase.Input(playlistId, moveList,
                if (mediaId.isPodcastPlaylist) PlaylistType.PODCAST else PlaylistType.TRACK
        ))
    }

    fun showSortByTutorialIfNeverShown(): Boolean {
        return tutorialPreferenceUseCase.sortByTutorial()
    }

}