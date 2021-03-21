package com.alimoradi.presentation.queue.di

import dagger.Subcomponent
import dagger.android.AndroidInjector
import com.alimoradi.presentation.dagger.PerFragment
import com.alimoradi.presentation.queue.PlayingQueueFragment

@Subcomponent(modules = [PlayingQueueFragmentModule::class])
@PerFragment
interface PlayingQueueFragmentSubComponent : AndroidInjector<PlayingQueueFragment> {

    @Subcomponent.Factory
    interface Builder : AndroidInjector.Factory<PlayingQueueFragment>

}