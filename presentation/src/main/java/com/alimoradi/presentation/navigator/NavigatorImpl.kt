package com.alimoradi.presentation.navigator

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.DefaultLifecycleObserver
import dagger.Lazy
import com.alimoradi.core.MediaId
import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.entity.PlaylistType
import com.alimoradi.presentation.createplaylist.CreatePlaylistFragment
import com.alimoradi.presentation.detail.DetailFragment
import com.alimoradi.presentation.dialogs.delete.DeleteDialog
import com.alimoradi.presentation.dialogs.favorite.AddFavoriteDialog
import com.alimoradi.presentation.dialogs.play.later.PlayLaterDialog
import com.alimoradi.presentation.dialogs.play.next.PlayNextDialog
import com.alimoradi.presentation.dialogs.playlist.clear.ClearPlaylistDialog
import com.alimoradi.presentation.dialogs.playlist.create.NewPlaylistDialog
import com.alimoradi.presentation.dialogs.playlist.duplicates.RemoveDuplicatesDialog
import com.alimoradi.presentation.dialogs.playlist.rename.RenameDialog
import com.alimoradi.presentation.dialogs.ringtone.SetRingtoneDialog
import com.alimoradi.presentation.edit.EditItemDialogFactory
import com.alimoradi.presentation.edit.album.EditAlbumFragment
import com.alimoradi.presentation.edit.artist.EditArtistFragment
import com.alimoradi.presentation.edit.song.EditTrackFragment
import com.alimoradi.presentation.interfaces.HasSlidingPanel
import com.alimoradi.presentation.offlinelyrics.OfflineLyricsFragment
import com.alimoradi.presentation.popup.PopupMenuFactory
import com.alimoradi.presentation.popup.main.MainPopupDialog
import com.alimoradi.presentation.recentlyadded.RecentlyAddedFragment
import com.alimoradi.presentation.relatedartists.RelatedArtistFragment
import com.alimoradi.presentation.splash.SplashFragment
import com.alimoradi.presentation.utils.collapse
import com.alimoradi.sharedandroid.extensions.fragmentTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import javax.inject.Inject

class NavigatorImpl @Inject internal constructor(
    activity: FragmentActivity,
    private val mainPopup: Lazy<MainPopupDialog>,
    private val popupFactory: Lazy<PopupMenuFactory>,
    private val editItemDialogFactory: Lazy<EditItemDialogFactory>

) : DefaultLifecycleObserver, Navigator {

    private val activityRef = WeakReference(activity)

    override fun toFirstAccess() {
        val activity = activityRef.get() ?: return
        activity.fragmentTransaction {
            add(android.R.id.content, SplashFragment(), SplashFragment.TAG)
        }
    }

    override fun toDetailFragment(mediaId: MediaId) {
        val activity = activityRef.get() ?: return
        (activity as HasSlidingPanel?)?.getSlidingPanel().collapse()

        val newTag = createBackStackTag(DetailFragment.TAG)
        superCerealTransition(
            activity,
            DetailFragment.newInstance(mediaId),
            newTag
        )
    }

    override fun toRelatedArtists(mediaId: MediaId) {
        val activity = activityRef.get() ?: return
        val newTag = createBackStackTag(RelatedArtistFragment.TAG)
        superCerealTransition(
            activity,
            RelatedArtistFragment.newInstance(mediaId),
            newTag
        )
    }

    override fun toRecentlyAdded(mediaId: MediaId) {
        val activity = activityRef.get() ?: return
        val newTag = createBackStackTag(RecentlyAddedFragment.TAG)
        superCerealTransition(
            activity,
            RecentlyAddedFragment.newInstance(mediaId),
            newTag
        )
    }

    override fun toOfflineLyrics() {
        val activity = activityRef.get() ?: return
        if (!allowed()) {
            return
        }
        activity.fragmentTransaction {
            setReorderingAllowed(true)
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            add(
                android.R.id.content,
                OfflineLyricsFragment.newInstance(),
                OfflineLyricsFragment.TAG
            )
            addToBackStack(OfflineLyricsFragment.TAG)
        }
    }

    override fun toEditInfoFragment(mediaId: MediaId) {
        val activity = activityRef.get() ?: return
        if (allowed()) {
            when {
                mediaId.isLeaf -> {
                    editItemDialogFactory.get().toEditTrack(mediaId) {
                        val instance = EditTrackFragment.newInstance(mediaId)
                        instance.show(activity.supportFragmentManager, EditTrackFragment.TAG)
                    }
                }
                mediaId.isAlbum || mediaId.isPodcastAlbum -> {
                    editItemDialogFactory.get().toEditAlbum(mediaId) {
                        val instance = EditAlbumFragment.newInstance(mediaId)
                        instance.show(activity.supportFragmentManager, EditAlbumFragment.TAG)
                    }
                }
                mediaId.isArtist || mediaId.isPodcastArtist -> {
                    editItemDialogFactory.get().toEditArtist(mediaId) {
                        val instance = EditArtistFragment.newInstance(mediaId)
                        instance.show(activity.supportFragmentManager, EditArtistFragment.TAG)
                    }
                }
                else -> throw IllegalArgumentException("invalid media id $mediaId")
            }
        }
    }

    override fun toChooseTracksForPlaylistFragment(type: PlaylistType) {
        val activity = activityRef.get() ?: return
        val newTag = createBackStackTag(CreatePlaylistFragment.TAG)
        superCerealTransition(
            activity,
            CreatePlaylistFragment.newInstance(type),
            newTag
        )
    }

    override fun toDialog(mediaId: MediaId, anchor: View) {
        if (allowed()) {
            GlobalScope.launch {
                val popup = popupFactory.get().create(anchor, mediaId)
                withContext(Dispatchers.Main) {
                    popup.show()
                }
            }
        }
    }

    override fun toMainPopup(anchor: View, category: MediaIdCategory?) {
        mainPopup.get().show(anchor, this, category)
    }

    override fun toSetRingtoneDialog(mediaId: MediaId, title: String, artist: String) {
        val activity = activityRef.get() ?: return
        val fragment = SetRingtoneDialog.newInstance(mediaId, title, artist)
        fragment.show(activity.supportFragmentManager, SetRingtoneDialog.TAG)
    }

    override fun toAddToFavoriteDialog(mediaId: MediaId, listSize: Int, itemTitle: String) {
        val activity = activityRef.get() ?: return
        val fragment = AddFavoriteDialog.newInstance(mediaId, listSize, itemTitle)
        fragment.show(activity.supportFragmentManager, AddFavoriteDialog.TAG)
    }

    override fun toPlayLater(mediaId: MediaId, listSize: Int, itemTitle: String) {
        val activity = activityRef.get() ?: return
        val fragment = PlayLaterDialog.newInstance(mediaId, listSize, itemTitle)
        fragment.show(activity.supportFragmentManager, PlayLaterDialog.TAG)
    }

    override fun toPlayNext(mediaId: MediaId, listSize: Int, itemTitle: String) {
        val activity = activityRef.get() ?: return
        val fragment = PlayNextDialog.newInstance(mediaId, listSize, itemTitle)
        fragment.show(activity.supportFragmentManager, PlayNextDialog.TAG)
    }

    override fun toRenameDialog(mediaId: MediaId, itemTitle: String) {
        val activity = activityRef.get() ?: return
        val fragment = RenameDialog.newInstance(mediaId, itemTitle)
        fragment.show(activity.supportFragmentManager, RenameDialog.TAG)
    }

    override fun toDeleteDialog(mediaId: MediaId, listSize: Int, itemTitle: String) {
        val activity = activityRef.get() ?: return
        val fragment = DeleteDialog.newInstance(mediaId, listSize, itemTitle)
        fragment.show(activity.supportFragmentManager, DeleteDialog.TAG)
    }

    override fun toCreatePlaylistDialog(mediaId: MediaId, listSize: Int, itemTitle: String) {
        val activity = activityRef.get() ?: return
        val fragment = NewPlaylistDialog.newInstance(mediaId, listSize, itemTitle)
        fragment.show(activity.supportFragmentManager, NewPlaylistDialog.TAG)
    }

    override fun toClearPlaylistDialog(mediaId: MediaId, itemTitle: String) {
        val activity = activityRef.get() ?: return
        val fragment = ClearPlaylistDialog.newInstance(mediaId, itemTitle)
        fragment.show(activity.supportFragmentManager, ClearPlaylistDialog.TAG)
    }

    override fun toRemoveDuplicatesDialog(mediaId: MediaId, itemTitle: String) {
        val activity = activityRef.get() ?: return
        val fragment = RemoveDuplicatesDialog.newInstance(mediaId, itemTitle)
        fragment.show(activity.supportFragmentManager, RemoveDuplicatesDialog.TAG)
    }
}
