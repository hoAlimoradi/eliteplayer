package com.alimoradi.presentation.search

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alimoradi.media.MediaProvider
import com.alimoradi.presentation.FloatingWindowHelper
import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.BaseFragment
import com.alimoradi.presentation.base.adapter.ObservableAdapter
import com.alimoradi.presentation.base.drag.DragListenerImpl
import com.alimoradi.presentation.base.drag.IDragListener
import com.alimoradi.presentation.interfaces.SetupNestedList
import com.alimoradi.presentation.navigator.Navigator
import com.alimoradi.presentation.search.adapter.SearchFragmentAdapter
import com.alimoradi.presentation.search.adapter.SearchFragmentNestedAdapter
import com.alimoradi.presentation.utils.hideIme
import com.alimoradi.presentation.utils.showIme
//import com.alimoradi.scrollhelper.layoutmanagers.OverScrollLinearLayoutManager
import com.alimoradi.sharedandroid.extensions.*
import com.alimoradi.shared.lazyFast
import dev.olog.scrollhelper.layoutmanagers.OverScrollLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : BaseFragment(),
    SetupNestedList,
    IDragListener by DragListenerImpl() {

    companion object {
        @JvmStatic
        val TAG = SearchFragment::class.java.name

        @JvmStatic
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazyFast {
        viewModelProvider<SearchFragmentViewModel>(
            viewModelFactory
        )
    }

    private val adapter by lazyFast {
        SearchFragmentAdapter(
            lifecycle,
            this,
            requireActivity() as MediaProvider,
            navigator,
            viewModel
        )
    }
    private val albumAdapter by lazyFast {
        SearchFragmentNestedAdapter(
            lifecycle,
            navigator,
            viewModel
        )
    }
    private val artistAdapter by lazyFast {
        SearchFragmentNestedAdapter(
            lifecycle,
            navigator,
            viewModel
        )
    }
    private val genreAdapter by lazyFast {
        SearchFragmentNestedAdapter(
            lifecycle,
            navigator,
            viewModel
        )
    }
    private val playlistAdapter by lazyFast {
        SearchFragmentNestedAdapter(
            lifecycle,
            navigator,
            viewModel
        )
    }

    private val folderAdapter by lazyFast {
        SearchFragmentNestedAdapter(
            lifecycle,
            navigator,
            viewModel
        )
    }
    private val recycledViewPool by lazyFast { RecyclerView.RecycledViewPool() }

    @Inject
    lateinit var navigator: Navigator
    private lateinit var layoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = OverScrollLinearLayoutManager(list)
        list.adapter = adapter
        list.layoutManager = layoutManager
        list.setRecycledViewPool(recycledViewPool)
        list.setHasFixedSize(true)

        setupDragListener(list, ItemTouchHelper.LEFT)

        viewModel.observeData()
            .subscribe(viewLifecycleOwner) {
                adapter.updateDataSet(it)
                emptyStateText.toggleVisibility(it.isEmpty(), true)
                restoreUpperWidgetsTranslation()
            }

        viewModel.observeAlbumsData()
            .subscribe(viewLifecycleOwner, albumAdapter::updateDataSet)

        viewModel.observeArtistsData()
            .subscribe(viewLifecycleOwner, artistAdapter::updateDataSet)

        viewModel.observePlaylistsData()
            .subscribe(viewLifecycleOwner, playlistAdapter::updateDataSet)

        viewModel.observeFoldersData()
            .subscribe(viewLifecycleOwner, folderAdapter::updateDataSet)

        viewModel.observeGenresData()
            .subscribe(viewLifecycleOwner, genreAdapter::updateDataSet)

        launch {
            editText.afterTextChange()
                .debounce(200)
                .filter { it.isBlank() || it.trim().length >= 2 }
                .collect { viewModel.updateQuery(it) }
        }
    }


    override fun setupNestedList(layoutId: Int, recyclerView: RecyclerView) {
        when (layoutId) {
            R.layout.item_search_list_albums -> setupHorizontalList(recyclerView, albumAdapter)
            R.layout.item_search_list_artists -> setupHorizontalList(recyclerView, artistAdapter)
            R.layout.item_search_list_folder -> setupHorizontalList(recyclerView, folderAdapter)
            R.layout.item_search_list_playlists -> setupHorizontalList(
                recyclerView,
                playlistAdapter
            )
            R.layout.item_search_list_genre -> setupHorizontalList(recyclerView, genreAdapter)
        }
    }

    private fun setupHorizontalList(list: RecyclerView, adapter: ObservableAdapter<*>) {
        val layoutManager = LinearLayoutManager(
            list.context,
            LinearLayoutManager.HORIZONTAL, false
        )
        list.layoutManager = layoutManager
        list.adapter = adapter
        list.setRecycledViewPool(recycledViewPool)
        list.setHasFixedSize(true)

        val snapHelper = androidx.recyclerview.widget.LinearSnapHelper()
        snapHelper.attachToRecyclerView(list)
    }

    override fun onResume() {
        super.onResume()
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        fab.setOnClickListener { editText.showIme() }

        floatingWindow.setOnClickListener { startServiceOrRequestOverlayPermission() }
        more.setOnClickListener { navigator.toMainPopup(it, null) }
    }

    override fun onPause() {
        super.onPause()
        act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
        fab.setOnClickListener(null)
        floatingWindow.setOnClickListener(null)
        more.setOnClickListener(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        list.adapter = null
    }

    private fun startServiceOrRequestOverlayPermission() {
        FloatingWindowHelper.startServiceOrRequestOverlayPermission(activity!!)
    }


    override fun onStop() {
        super.onStop()
        editText.hideIme()
    }

    override fun provideLayoutId(): Int = R.layout.fragment_search

}