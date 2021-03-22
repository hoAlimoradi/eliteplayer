package com.alimoradi.presentation.chooser.di

import dagger.Component
import dagger.android.AndroidInjectionModule
import com.alimoradi.injection.CoreComponent
import com.alimoradi.presentation.ViewModelModule
import com.alimoradi.presentation.dagger.PerActivity
import com.alimoradi.presentation.chooser.PlaylistChooserActivity

fun PlaylistChooserActivity.inject() {
    DaggerPlaylistChooserActivityComponent.factory()
        .create(CoreComponent.coreComponent(application))
        .inject(this)
}

@Component(
    modules = [
        AndroidInjectionModule::class,
        ViewModelModule::class,
        PlaylistChooserActivityModule::class
    ], dependencies = [CoreComponent::class]
)
@PerActivity
interface PlaylistChooserActivityComponent {

    fun inject(instance: PlaylistChooserActivity)

    @Component.Factory
    interface Factory {

        fun create(component: CoreComponent): PlaylistChooserActivityComponent
    }

}