package com.alimoradi.presentation.edit.artist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alimoradi.core.MediaId
import com.alimoradi.core.entity.track.Artist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jaudiotagger.tag.TagOptionSingleton
import javax.inject.Inject

class EditArtistFragmentViewModel @Inject constructor(
    private val presenter: EditArtistFragmentPresenter

) : ViewModel() {

    init {
        TagOptionSingleton.getInstance().isAndroid = true
    }

    private val displayableArtistLiveData = MutableLiveData<DisplayableArtist>()

    fun requestData(mediaId: MediaId) = viewModelScope.launch {
        val artist = withContext(Dispatchers.IO) {
            presenter.getArtist(mediaId)
        }
        displayableArtistLiveData.value = artist.toDisplayableArtist()
    }

    fun observeData(): LiveData<DisplayableArtist> = displayableArtistLiveData


    override fun onCleared() {
        viewModelScope.cancel()
    }

    private fun Artist.toDisplayableArtist(): DisplayableArtist {
        return DisplayableArtist(
            id = this.id,
            title = this.name,
            albumArtist = this.albumArtist,
            songs = this.songs,
            isPodcast = this.isPodcast
        )
    }


}