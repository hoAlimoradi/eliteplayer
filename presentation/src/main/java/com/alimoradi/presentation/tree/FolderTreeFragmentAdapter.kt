package com.alimoradi.presentation.folder.tree

import androidx.lifecycle.Lifecycle
import com.alimoradi.media.MediaProvider
import com.alimoradi.presentation.BindingsAdapter
import com.alimoradi.presentation.R
import com.alimoradi.presentation.base.adapter.DataBoundViewHolder
import com.alimoradi.presentation.base.adapter.ObservableAdapter
import com.alimoradi.presentation.base.adapter.setOnClickListener
import com.alimoradi.presentation.base.adapter.setOnLongClickListener
import com.alimoradi.presentation.dagger.FragmentLifecycle
import com.alimoradi.presentation.model.DisplayableFile
import com.alimoradi.presentation.navigator.Navigator
import kotlinx.android.synthetic.main.item_detail_related_artist.view.*

class FolderTreeFragmentAdapter(
    @FragmentLifecycle lifecycle: Lifecycle,
    private val viewModel: FolderTreeFragmentViewModel,
    private val mediaProvider: MediaProvider,
    private val navigator: Navigator

) : ObservableAdapter<DisplayableFile>(lifecycle, DiffCallbackDisplayableFile) {

    override fun initViewHolderListeners(viewHolder: DataBoundViewHolder, viewType: Int) {
        when (viewType) {
            R.layout.item_folder_tree_directory,
            R.layout.item_folder_tree_track -> {
                viewHolder.setOnClickListener(this) { item, _, _ ->
                    when {
                        item.mediaId == FolderTreeFragmentViewModel.BACK_HEADER_ID -> viewModel.popFolder()
                        item.isFile() && item.asFile().isDirectory -> viewModel.nextFolder(item.asFile())
                        else -> {
                            viewModel.createMediaId(item)?.let { mediaId ->
                                mediaProvider.playFromMediaId(mediaId, null, null)
                            }

                        }
                    }
                }
                viewHolder.setOnLongClickListener(this) { item, _, view ->
                    if (item.mediaId == FolderTreeFragmentViewModel.BACK_HEADER_ID) {
                        return@setOnLongClickListener
                    }
                    if (!item.asFile().isDirectory) {
                        viewModel.createMediaId(item)?.let { mediaId ->
                            navigator.toDialog(mediaId, view)
                        }
                    }
                }
            }
        }

    }

    override fun bind(holder: DataBoundViewHolder, item: DisplayableFile, position: Int) {
        holder.itemView.apply {
            firstText.text = item.title
        }
        when (holder.itemViewType){
            R.layout.item_folder_tree_directory -> {
                BindingsAdapter.loadDirImage(holder.imageView!!, item)
            }
            R.layout.item_folder_tree_track -> {
                BindingsAdapter.loadFile(holder.imageView!!, item)
            }
        }
    }
}