package com.alimoradi.presentation.recentlyadded.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dev.olog.core.MediaId
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.recentlyadded.RecentlyAddedFragment
import com.alimoradi.presentation.recentlyadded.RecentlyAddedFragmentViewModel
import com.alimoradi.sharedandroid.extensions.getArgument

@Module
abstract class RecentlyAddedFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(RecentlyAddedFragmentViewModel::class)
    abstract fun provideViewModel(factory: RecentlyAddedFragmentViewModel): ViewModel


    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideMediaId(instance: RecentlyAddedFragment): MediaId {
            val mediaId = instance.getArgument<String>(RecentlyAddedFragment.ARGUMENTS_MEDIA_ID)
            return MediaId.fromString(mediaId)
        }

    }

}