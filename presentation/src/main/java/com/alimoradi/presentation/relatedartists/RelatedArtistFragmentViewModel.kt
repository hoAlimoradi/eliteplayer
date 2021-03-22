package com.alimoradi.presentation.relatedartists

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.track.Artist
import com.alimoradi.core.interactor.GetItemTitleUseCase
import com.alimoradi.core.interactor.ObserveRelatedArtistsUseCase
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.DisplayableAlbum
import com.alimoradi.presentation.model.DisplayableItem
import com.alimoradi.shared.mapListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class RelatedArtistFragmentViewModel @Inject constructor(
    resources: Resources,
    mediaId: MediaId,
    useCase: ObserveRelatedArtistsUseCase,
    getItemTitleUseCase: GetItemTitleUseCase

) : ViewModel() {

    val itemOrdinal = mediaId.category.ordinal

    private val liveData = MutableLiveData<List<DisplayableItem>>()
    private val titleLiveData = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            useCase(mediaId)
                .mapListItem { it.toRelatedArtist(resources) }
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

    private fun Artist.toRelatedArtist(resources: Resources): DisplayableItem {
        val songs =
            resources.getQuantityString(R.plurals.common_plurals_song, this.songs, this.songs)

        return DisplayableAlbum(
            type = R.layout.item_related_artist,
            mediaId = getMediaId(),
            title = this.name,
            subtitle = songs
        )
    }

}