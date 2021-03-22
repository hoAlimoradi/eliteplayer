package com.alimoradi.presentation.tree

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alimoradi.media.MediaProvider
import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.BaseFragment
import com.alimoradi.presentation.interfaces.CanHandleOnBackPressed
import com.alimoradi.presentation.navigator.Navigator
import com.alimoradi.presentation.widgets.BreadCrumbLayout
import dev.olog.scrollhelper.layoutmanagers.OverScrollLinearLayoutManager
import com.alimoradi.sharedandroid.extensions.ctx
import com.alimoradi.sharedandroid.extensions.dimen
import com.alimoradi.sharedandroid.extensions.subscribe
import com.alimoradi.sharedandroid.extensions.viewModelProvider
import com.alimoradi.shared.clamp
import com.alimoradi.shared.lazyFast
import kotlinx.android.synthetic.main.fragment_folder_tree.*
import javax.inject.Inject

class FolderTreeFragment : BaseFragment(),
    BreadCrumbLayout.SelectionCallback,
    CanHandleOnBackPressed {

    companion object {

        @JvmStatic
        fun newInstance(): FolderTreeFragment {
            return FolderTreeFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigator: Navigator
    private val viewModel by lazyFast {
        viewModelProvider<FolderTreeFragmentViewModel>(
            viewModelFactory
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = FolderTreeFragmentAdapter(
            lifecycle,
            viewModel,
            activity as MediaProvider,
            navigator
        )
        fab.shrink()

        list.adapter = adapter
        list.layoutManager = OverScrollLinearLayoutManager(list)
        list.setHasFixedSize(true)

        fastScroller.attachRecyclerView(list)
        fastScroller.showBubble(false)

        viewModel.observeCurrentDirectoryFileName()
            .subscribe(viewLifecycleOwner) {
                bread_crumbs.setActiveOrAdd(BreadCrumbLayout.Crumb(it), false)
            }

        viewModel.observeChildren()
            .subscribe(viewLifecycleOwner, adapter::updateDataSet)

        viewModel.observeCurrentFolderIsDefaultFolder()
            .subscribe(viewLifecycleOwner) { isDefaultFolder ->
                if (isDefaultFolder){
                    fab.hide()
                } else {
                    fab.show()
                }
            }
    }

    override fun onResume() {
        super.onResume()
        bread_crumbs.setCallback(this)
        list.addOnScrollListener(scrollListener)
        fab.setOnClickListener { onFabClick() }
    }

    override fun onPause() {
        super.onPause()
        bread_crumbs.setCallback(null)
        list.removeOnScrollListener(scrollListener)
        fab.setOnClickListener(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        list.adapter = null
    }

    private fun onFabClick(){
        if (!fab.isExtended){
            fab.extend()
            return
        }
        viewModel.updateDefaultFolder()
    }

    override fun onCrumbSelection(crumb: BreadCrumbLayout.Crumb, index: Int) {
        viewModel.nextFolder(crumb.file.absoluteFile)
    }

    override fun handleOnBackPressed(): Boolean {
        return viewModel.popFolder()
    }

    private val scrollListener = object : RecyclerView.OnScrollListener(){

        private val toolbarHeight by lazyFast { ctx.dimen(R.dimen.toolbar) }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val currentTranlationY = crumbsWrapper.translationY
            val clampedTranslation = clamp(currentTranlationY - dy, -toolbarHeight.toFloat(), 0f)
            crumbsWrapper.translationY = clampedTranslation
        }
    }

    override fun provideLayoutId(): Int = R.layout.fragment_folder_tree
}