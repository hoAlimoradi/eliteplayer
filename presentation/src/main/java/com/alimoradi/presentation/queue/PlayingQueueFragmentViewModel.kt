package com.alimoradi.presentation.queue

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alimoradi.core.entity.PlayingQueueSong
import com.alimoradi.core.gateway.PlayingQueueGateway
import com.alimoradi.core.prefs.MusicPreferencesGateway
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.DisplayableQueueSong
import com.alimoradi.sharedandroid.extensions.assertBackground
import com.alimoradi.shared.swap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class PlayingQueueFragmentViewModel @Inject constructor(
    private val musicPreferencesUseCase: MusicPreferencesGateway,
    playingQueueGateway: PlayingQueueGateway

) : ViewModel() {

    fun getLastIdInPlaylist() = musicPreferencesUseCase.getLastIdInPlaylist()

    private val data = MutableLiveData<List<DisplayableQueueSong>>()

    private val queueLiveData = ConflatedBroadcastChannel<List<PlayingQueueSong>>()

    init {
        viewModelScope.launch {
            playingQueueGateway.observeAll().distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect { queueLiveData.offer(it) }
        }

        viewModelScope.launch {
            queueLiveData.asFlow()
                .combine(musicPreferencesUseCase.observeLastIdInPlaylist().distinctUntilChanged())
                { queue, idInPlaylist ->
                    val currentPlayingIndex = queue.indexOfFirst { it.song.idInPlaylist == idInPlaylist }
                    queue.mapIndexed { index, item ->
                        item.toDisplayableItem(index, currentPlayingIndex, idInPlaylist)
                    }
                }
                .assertBackground()
                .flowOn(Dispatchers.Default)
                .collect {
                    data.value = it
                }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }

    fun observeData(): LiveData<List<DisplayableQueueSong>> = data

    fun recalculatePositionsAfterRemove(position: Int) =
        viewModelScope.launch(Dispatchers.Default) {
            val currentList = queueLiveData.value.toMutableList()
            currentList.removeAt(position)

            queueLiveData.offer(currentList)
        }

    /**
     * @param moves contains all the movements in the list
     */
    fun recalculatePositionsAfterMove(moves: List<Pair<Int, Int>>) =
        viewModelScope.launch(Dispatchers.Default) {
            val currentList = queueLiveData.value
            for ((from, to) in moves) {
                currentList.swap(from, to)
            }

            queueLiveData.offer(currentList)
        }

    private fun PlayingQueueSong.toDisplayableItem(
        currentPosition: Int,
        currentPlayingIndex: Int,
        currentPlayingIdInPlaylist: Int
    ): DisplayableQueueSong {
        val song = this.song

        val relativePosition = computeRelativePosition(currentPosition, currentPlayingIndex)

        return DisplayableQueueSong(
            type = R.layout.item_playing_queue,
            mediaId = mediaId,
            title = song.title,
            artist = song.artist,
            album = song.album,
            idInPlaylist = song.idInPlaylist,
            relativePosition = relativePosition,
            isCurrentSong = song.idInPlaylist == currentPlayingIdInPlaylist
        )
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun computeRelativePosition(
        currentPosition: Int,
        currentPlayingIndex: Int
    ): String {
        return when {
            currentPosition > currentPlayingIndex -> "+${currentPosition - currentPlayingIndex}"
            currentPosition < currentPlayingIndex -> "${currentPosition - currentPlayingIndex}"
            else -> "-"
        }
    }
}