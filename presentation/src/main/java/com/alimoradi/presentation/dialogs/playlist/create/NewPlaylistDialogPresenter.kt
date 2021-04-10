package com.alimoradi.presentation.dialogs.playlist.create

import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.PlaylistType
import com.alimoradi.core.gateway.PlayingQueueGateway
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.core.interactor.playlist.InsertCustomTrackListRequest
import com.alimoradi.core.interactor.playlist.InsertCustomTrackListToPlaylist
import com.alimoradi.core.interactor.songlist.GetSongListByParamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewPlaylistDialogPresenter @Inject constructor(
    private val insertCustomTrackListToPlaylist: InsertCustomTrackListToPlaylist,
    private val getSongListByParamUseCase: GetSongListByParamUseCase,
    private val playingQueueGateway: PlayingQueueGateway,
    private val podcastGateway: PodcastGateway,
    private val songGateway: SongGateway

) {

    suspend fun execute(mediaId: MediaId, playlistTitle: String) = withContext(Dispatchers.IO) {
        val playlistType = if (mediaId.isPodcast) PlaylistType.PODCAST else PlaylistType.TRACK

        val trackToInsert = when {
            mediaId.isPlayingQueue -> playingQueueGateway.getAll().map { it.song.id }
            mediaId.isLeaf && mediaId.isPodcast -> listOf(podcastGateway.getByParam(mediaId.resolveId)!!.id)
            mediaId.isLeaf -> listOf(songGateway.getByParam(mediaId.resolveId)!!.id)
            else -> getSongListByParamUseCase(mediaId).map { it.id }
        }
        insertCustomTrackListToPlaylist(InsertCustomTrackListRequest(playlistTitle, trackToInsert, playlistType))
    }

}