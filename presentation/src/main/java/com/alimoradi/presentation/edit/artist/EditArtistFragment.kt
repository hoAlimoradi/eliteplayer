package com.alimoradi.presentation.edit.artist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import dev.olog.core.MediaId
import com.alimoradi.presentation.R
import com.alimoradi.presentation.edit.BaseEditItemFragment
import com.alimoradi.presentation.edit.EditItemViewModel
import com.alimoradi.presentation.edit.UpdateArtistInfo
import com.alimoradi.presentation.edit.model.UpdateResult
import com.alimoradi.sharedandroid.extensions.*
import com.alimoradi.shared.lazyFast
import kotlinx.android.synthetic.main.fragment_edit_artist.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditArtistFragment : BaseEditItemFragment() {

    companion object {
        const val TAG = "EditArtistFragment"
        const val ARGUMENTS_MEDIA_ID = "$TAG.arguments.media_id"

        @JvmStatic
        fun newInstance(mediaId: MediaId): EditArtistFragment {
            return EditArtistFragment().withArguments(
                ARGUMENTS_MEDIA_ID to mediaId.toString()
            )
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazyFast {
        viewModelProvider<EditArtistFragmentViewModel>(
            viewModelFactory
        )
    }
    private val editItemViewModel by lazyFast {
        activity!!.viewModelProvider<EditItemViewModel>(
            viewModelFactory
        )
    }

    private val mediaId by lazyFast {
        MediaId.fromString(getArgument(ARGUMENTS_MEDIA_ID))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.requestData(mediaId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch {
            artist.afterTextChange()
                .map { it.isNotBlank() }
                .collect { okButton.isEnabled = it }
        }

        loadImage(mediaId)

        viewModel.observeData().subscribe(viewLifecycleOwner) {
            artist.setText(it.title)
            albumArtist.setText(it.albumArtist)
            val text = resources.getQuantityString(
                R.plurals.edit_item_xx_tracks_will_be_updated, it.songs, it.songs
            )
            albumsUpdated.text = text
            podcast.isChecked = it.isPodcast
        }
    }

    override fun onResume() {
        super.onResume()
        okButton.setOnClickListener {
            launch { trySave() }
        }
        cancelButton.setOnClickListener { dismiss() }
    }

    override fun onPause() {
        super.onPause()
        okButton.setOnClickListener(null)
        cancelButton.setOnClickListener(null)
    }

    private suspend fun trySave(){
        val result = editItemViewModel.updateArtist(
            UpdateArtistInfo(
                mediaId,
                artist.extractText().trim(),
                albumArtist.extractText().trim(),
                podcast.isChecked
            )
        )

        when (result){
            UpdateResult.OK -> dismiss()
            UpdateResult.EMPTY_TITLE -> ctx.toast(R.string.edit_artist_invalid_title)
            else -> {}
        }
    }

    override fun onLoaderCancelled() {
    }

    override fun provideLayoutId(): Int = R.layout.fragment_edit_artist
}