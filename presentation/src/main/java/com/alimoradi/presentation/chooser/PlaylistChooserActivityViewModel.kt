package com.alimoradi.presentation.chooser

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alimoradi.core.dagger.ApplicationContext
import com.alimoradi.core.entity.track.Playlist
import com.alimoradi.core.gateway.track.PlaylistGateway
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

class PlaylistChooserActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val playlistGateway: PlaylistGateway
) : ViewModel() {

    private val data = MutableLiveData<List<DisplayableItem>>()

    init {
        viewModelScope.launch {
            playlistGateway.observeAll()
                .mapListItem { it.toDisplayableItem(context.resources) }
                .flowOn(Dispatchers.IO)
                .collect { data.value = it }
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
    }

    fun observeData(): LiveData<List<DisplayableItem>> = data

    private fun Playlist.toDisplayableItem(resources: Resources): DisplayableItem {
        return DisplayableAlbum(
            type = R.layout.item_playlist_chooser,
            mediaId = getMediaId(),
            title = title,
            subtitle = DisplayableAlbum.readableSongCount(resources, size)
        )
    }

}