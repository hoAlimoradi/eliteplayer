package com.alimoradi.presentation.createplaylist

import android.util.LongSparseArray
import androidx.core.util.contains
import androidx.core.util.isEmpty
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.PlaylistType
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.gateway.podcast.PodcastGateway
import com.alimoradi.core.gateway.track.SongGateway
import com.alimoradi.core.interactor.playlist.InsertCustomTrackListRequest
import com.alimoradi.core.interactor.playlist.InsertCustomTrackListToPlaylist
import com.alimoradi.presentation.createplaylist.mapper.toDisplayableItem
import com.alimoradi.presentation.model.DisplayableItem
import com.alimoradi.shared.mapListItem
import com.alimoradi.sharedandroid.extensions.toList
import com.alimoradi.sharedandroid.extensions.toggle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreatePlaylistFragmentViewModel @Inject constructor(
    private val playlistType: PlaylistType,
    private val getAllSongsUseCase: SongGateway,
    private val getAllPodcastsUseCase: PodcastGateway,
    private val insertCustomTrackListToPlaylist: InsertCustomTrackListToPlaylist

) : ViewModel() {

    private val data = MutableLiveData<List<DisplayableItem>>()

    private val selectedIds = LongSparseArray<Long>()
    private val selectionCountLiveData = MutableLiveData<Int>()
    private val showOnlyFiltered = ConflatedBroadcastChannel(false)

    private val filterChannel = ConflatedBroadcastChannel("")

    init {
        viewModelScope.launch {
            showOnlyFiltered.asFlow()
                .flatMapLatest { onlyFiltered ->
                    if (onlyFiltered){
                        getPlaylistTypeTracks().map { songs -> songs.filter { selectedIds.contains(it.id) } }
                    } else {
                        getPlaylistTypeTracks().combine(filterChannel.asFlow()) { tracks, filter ->
                            if (filter.isNotEmpty()) {
                                tracks.filter {
                                    it.title.contains(filter, true) ||
                                            it.artist.contains(filter, true) ||
                                            it.album.contains(filter, true)
                                }
                            } else {
                                tracks
                            }
                        }
                    }
                }.mapListItem { it.toDisplayableItem() }
                .flowOn(Dispatchers.Default)
                .collect { data.value = it }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }

    fun updateFilter(filter: String) {
        filterChannel.offer(filter)
    }

    fun observeData(): LiveData<List<DisplayableItem>> = data

    private fun getPlaylistTypeTracks(): Flow<List<Song>> = when (playlistType) {
        PlaylistType.PODCAST -> getAllPodcastsUseCase.observeAll()
        PlaylistType.TRACK -> getAllSongsUseCase.observeAll()
        PlaylistType.AUTO -> throw IllegalArgumentException("type auto not valid")
    }

    fun toggleItem(mediaId: MediaId) {
        val id = mediaId.resolveId
        selectedIds.toggle(id, id)
        selectionCountLiveData.postValue(selectedIds.size())
    }

    fun toggleShowOnlyFiltered() {
        val onlyFiltered = showOnlyFiltered.value
        showOnlyFiltered.offer(!onlyFiltered)
    }

    fun isChecked(mediaId: MediaId): Boolean {
        val id = mediaId.resolveId
        return selectedIds[id] != null
    }

    fun observeSelectedCount(): LiveData<Int> = selectionCountLiveData

    suspend fun savePlaylist(playlistTitle: String): Boolean {
        if (selectedIds.isEmpty()) {
            throw IllegalStateException("not supposed to happen, save button must be invisible")
        }
        withContext(Dispatchers.IO){
            insertCustomTrackListToPlaylist(
                InsertCustomTrackListRequest(
                    playlistTitle,
                    selectedIds.toList(),
                    playlistType
                )
            )
        }

        return true
    }

}