package com.alimoradi.presentation.relatedartists

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import dev.olog.core.MediaId
import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.BaseFragment
import com.alimoradi.presentation.navigator.Navigator
import dev.olog.scrollhelper.layoutmanagers.OverScrollGridLayoutManager
import com.alimoradi.sharedandroid.extensions.act
import com.alimoradi.sharedandroid.extensions.subscribe
import com.alimoradi.sharedandroid.extensions.viewModelProvider
import com.alimoradi.sharedandroid.extensions.withArguments
import com.alimoradi.shared.lazyFast
import kotlinx.android.synthetic.main.fragment_related_artist.*
import javax.inject.Inject

class RelatedArtistFragment : BaseFragment() {

    companion object {
        @JvmStatic
        val TAG = RelatedArtistFragment::class.java.name
        @JvmStatic
        val ARGUMENTS_MEDIA_ID = "$TAG.arguments.media_id"

        @JvmStatic
        fun newInstance(mediaId: MediaId): RelatedArtistFragment {
            return RelatedArtistFragment().withArguments(
                ARGUMENTS_MEDIA_ID to mediaId.toString()
            )
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigator: Navigator
    private val adapter by lazyFast { RelatedArtistFragmentAdapter(lifecycle, navigator) }

    private val viewModel by lazyFast {
        viewModelProvider<RelatedArtistFragmentViewModel>(
            viewModelFactory
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list.layoutManager = OverScrollGridLayoutManager(list, 2)
        list.adapter = adapter
        list.setHasFixedSize(true)

        viewModel.observeData()
            .subscribe(viewLifecycleOwner, adapter::updateDataSet)

        viewModel.observeTitle()
            .subscribe(viewLifecycleOwner) { itemTitle ->
                val headersArray = resources.getStringArray(R.array.related_artists_header)
                val header = String.format(headersArray[viewModel.itemOrdinal], itemTitle)
                this.header.text = header
            }
    }

    override fun onResume() {
        super.onResume()
        back.setOnClickListener { act.onBackPressed() }
    }

    override fun onPause() {
        super.onPause()
        back.setOnClickListener(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        list.adapter = null
    }

    override fun provideLayoutId(): Int = R.layout.fragment_related_artist
}