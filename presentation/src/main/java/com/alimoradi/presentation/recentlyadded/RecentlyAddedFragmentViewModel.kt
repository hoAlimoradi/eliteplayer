package com.alimoradi.presentation.recentlyadded

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.track.Song
import com.alimoradi.core.interactor.GetItemTitleUseCase
import com.alimoradi.core.interactor.ObserveRecentlyAddedUseCase
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.DisplayableItem
import com.alimoradi.presentation.model.DisplayableTrack
import com.alimoradi.shared.mapListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecentlyAddedFragmentViewModel @Inject constructor(
    mediaId: MediaId,
    useCase: ObserveRecentlyAddedUseCase,
    getItemTitleUseCase: GetItemTitleUseCase

) : ViewModel() {

    val itemOrdinal = mediaId.category.ordinal

    private val liveData = MutableLiveData<List<DisplayableItem>>()
    private val titleLiveData = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            useCase(mediaId)
                .mapListItem { it.toRecentDetailDisplayableItem(mediaId) }
                .flowOn(Dispatchers.IO)
                .collect { liveData.value = it }
        }
        viewModelScope.launch {
            getItemTitleUseCase(mediaId)
                .flowOn(Dispatchers.IO)
                .collect { titleLiveData.value = it }
        }
    }

    fun observeData(): LiveData<List<DisplayableItem>> = liveData
    fun observeTitle(): LiveData<String> = titleLiveData

    override fun onCleared() {
        viewModelScope.cancel()
    }

    private fun Song.toRecentDetailDisplayableItem(parentId: MediaId): DisplayableItem {
        return DisplayableTrack(
            type = R.layout.item_recently_added,
            mediaId = MediaId.playableItem(parentId, id),
            title = title,
            artist = artist,
            album = album,
            idInPlaylist = idInPlaylist,
            dataModified = this.dateModified
        )
    }


}
