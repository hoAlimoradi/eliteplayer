package com.alimoradi.presentation.playlist.chooser

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.BaseActivity
import com.alimoradi.presentation.playlist.chooser.di.inject
import com.alimoradi.sharedandroid.extensions.subscribe
import com.alimoradi.sharedandroid.extensions.toast
import com.alimoradi.sharedandroid.extensions.viewModelProvider
import com.alimoradi.shared.lazyFast
import kotlinx.android.synthetic.main.activity_playlist_chooser.*
import javax.inject.Inject

class PlaylistChooserActivity : BaseActivity() {

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private val viewModel by lazyFast { viewModelProvider<PlaylistChooserActivityViewModel>(factory) }

    private val adapter by lazyFast { PlaylistChooserActivityAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_chooser)

        viewModel.observeData()
            .subscribe(this) { list ->
                if (list.isEmpty()){
                    toast("No playlist found")
                    finish()
                } else {
                    adapter.updateDataSet(list)
                }
            }

        list.adapter = adapter
        list.layoutManager = GridLayoutManager(this, 2)
    }

    override fun onResume() {
        super.onResume()
        back.setOnClickListener { finish() }
    }

    override fun onPause() {
        super.onPause()
        back.setOnClickListener(null)
    }

}