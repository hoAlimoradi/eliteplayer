package com.alimoradi.presentation.relatedartists.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dev.olog.core.MediaId
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.relatedartists.RelatedArtistFragment
import com.alimoradi.presentation.relatedartists.RelatedArtistFragmentViewModel
import com.alimoradi.sharedandroid.extensions.getArgument

@Module
abstract class RelatedArtistFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(RelatedArtistFragmentViewModel::class)
    abstract fun provideViewModel(factory: RelatedArtistFragmentViewModel): ViewModel

    @Module
    companion object {
        @Provides
        @JvmStatic
        internal fun provideMediaId(instance: RelatedArtistFragment): MediaId {
            val mediaId = instance.getArgument<String>(RelatedArtistFragment.ARGUMENTS_MEDIA_ID)
            return MediaId.fromString(mediaId)
        }
    }

}