package com.alimoradi.presentation.detail.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dev.olog.core.MediaId
import com.alimoradi.presentation.dagger.ViewModelKey
import com.alimoradi.presentation.detail.DetailFragment
import com.alimoradi.presentation.detail.DetailFragmentViewModel
import com.alimoradi.sharedandroid.extensions.getArgument

@Module
internal abstract class DetailFragmentModule {

    @Binds
    @IntoMap
    @ViewModelKey(DetailFragmentViewModel::class)
    abstract fun provideViewModel(viewModel: DetailFragmentViewModel): ViewModel

    @Module
    companion object {

        @JvmStatic
        @Provides
        internal fun provideMediaId(instance: DetailFragment): MediaId {
            val mediaId = instance.getArgument<String>(DetailFragment.ARGUMENTS_MEDIA_ID)
            return MediaId.fromString(mediaId)
        }

    }


}