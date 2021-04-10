package com.alimoradi.presentation.dialogs

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.alimoradi.presentation.dialogs.delete.DeleteDialog
import com.alimoradi.presentation.dialogs.favorite.AddFavoriteDialog
import com.alimoradi.presentation.dialogs.play.later.PlayLaterDialog
import com.alimoradi.presentation.dialogs.play.next.PlayNextDialog
import com.alimoradi.presentation.dialogs.playlist.clear.ClearPlaylistDialog
import com.alimoradi.presentation.dialogs.playlist.create.NewPlaylistDialog
import com.alimoradi.presentation.dialogs.playlist.duplicates.RemoveDuplicatesDialog
import com.alimoradi.presentation.dialogs.playlist.rename.RenameDialog
import com.alimoradi.presentation.dialogs.ringtone.SetRingtoneDialog

@Module
abstract class DialogModule {

    @ContributesAndroidInjector
    abstract fun provideDeleteDialog(): DeleteDialog

    @ContributesAndroidInjector
    abstract fun provideAddFavoriteDialog(): AddFavoriteDialog

    @ContributesAndroidInjector
    abstract fun providePlayNextDialog(): PlayNextDialog

    @ContributesAndroidInjector
    abstract fun providePlayLaterDialog(): PlayLaterDialog

    @ContributesAndroidInjector
    abstract fun provideClearPlaylistDialog(): ClearPlaylistDialog

    @ContributesAndroidInjector
    abstract fun provideCreatePlaylistDialog(): NewPlaylistDialog

    @ContributesAndroidInjector
    abstract fun provideRemoveDuplicatesDialog(): RemoveDuplicatesDialog

    @ContributesAndroidInjector
    abstract fun provideRenametDialog(): RenameDialog

    @ContributesAndroidInjector
    abstract fun provideSetRingtoneDialog(): SetRingtoneDialog

}