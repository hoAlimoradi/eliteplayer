package com.alimoradi.presentation.recentlyadded

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import dev.olog.core.MediaId
import com.alimoradi.media.MediaProvider
import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.BaseFragment
import com.alimoradi.presentation.base.drag.DragListenerImpl
import com.alimoradi.presentation.base.drag.IDragListener
import com.alimoradi.presentation.navigator.Navigator
import dev.olog.scrollhelper.layoutmanagers.OverScrollLinearLayoutManager
import com.alimoradi.sharedandroid.extensions.act
import com.alimoradi.sharedandroid.extensions.subscribe
import com.alimoradi.sharedandroid.extensions.viewModelProvider
import com.alimoradi.sharedandroid.extensions.withArguments
import com.alimoradi.shared.lazyFast
import kotlinx.android.synthetic.main.fragment_recently_added.*
import javax.inject.Inject

class RecentlyAddedFragment : BaseFragment(), IDragListener by DragListenerImpl() {

    companion object {
        @JvmStatic
        val TAG = RecentlyAddedFragment::class.java.name
        @JvmStatic
        val ARGUMENTS_MEDIA_ID = "$TAG.arguments.media_id"

        @JvmStatic
        fun newInstance(mediaId: MediaId): RecentlyAddedFragment {
            return RecentlyAddedFragment().withArguments(
                ARGUMENTS_MEDIA_ID to mediaId.toString()
            )
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigator: Navigator
    private val adapter by lazyFast {
        RecentlyAddedFragmentAdapter(
            lifecycle, navigator, act as MediaProvider, this
        )
    }

    private val viewModel by lazyFast {
        viewModelProvider<RecentlyAddedFragmentViewModel>(
            viewModelFactory
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        list.adapter = adapter
        list.layoutManager = OverScrollLinearLayoutManager(list)
        list.setHasFixedSize(true)

        setupDragListener(list, ItemTouchHelper.LEFT)

        viewModel.observeData().subscribe(viewLifecycleOwner, adapter::updateDataSet)

        viewModel.observeTitle()
            .subscribe(viewLifecycleOwner) { itemTitle ->
                val headersArray = resources.getStringArray(R.array.recently_added_header)
                val header = String.format(headersArray[viewModel.itemOrdinal], itemTitle)
                this.header.text = header
            }
    }

    override fun onResume() {
        super.onResume()
        back.setOnClickListener { activity!!.onBackPressed() }
    }

    override fun onPause() {
        super.onPause()
        back.setOnClickListener(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        list.adapter = null
    }

    override fun provideLayoutId(): Int = R.layout.fragment_recently_added
}