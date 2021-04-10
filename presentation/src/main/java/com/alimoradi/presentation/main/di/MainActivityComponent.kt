package com.alimoradi.presentation.main.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import com.alimoradi.injection.CoreComponent
import com.alimoradi.presentation.ViewModelModule
import com.alimoradi.presentation.about.di.AboutFragmentModule
import com.alimoradi.presentation.createplaylist.di.CreatePlaylistFragmentInjector
import com.alimoradi.presentation.dagger.PerActivity
import com.alimoradi.presentation.detail.di.DetailFragmentInjector
import com.alimoradi.presentation.dialogs.DialogModule
import com.alimoradi.presentation.edit.di.EditItemModule
import com.alimoradi.presentation.equalizer.EqualizerModule
import com.alimoradi.presentation.tree.di.FolderTreeFragmentModule
import com.alimoradi.presentation.main.MainActivity
import com.alimoradi.presentation.model.PresentationModelModule
import com.alimoradi.presentation.player.di.PlayerFragmentModule
import com.alimoradi.presentation.prefs.di.SettingsFragmentsModule
import com.alimoradi.presentation.queue.di.PlayingQueueFragmentInjector
import com.alimoradi.presentation.recentlyadded.di.RecentlyAddedFragmentInjector
import com.alimoradi.presentation.relatedartists.di.RelatedArtistFragmentInjector
import com.alimoradi.presentation.search.di.SearchFragmentInjector
import com.alimoradi.presentation.tab.di.TabFragmentInjector
import com.alimoradi.presentation.widgets.bottomnavigator.CustomBottomNavigator
import com.alimoradi.sharedandroid.utils.assertMainThread
import java.lang.ref.WeakReference

private var activityComponent: WeakReference<MainActivityComponent>? = null

private fun buildComponent(activity: MainActivity): MainActivityComponent {
    assertMainThread()

    if (activityComponent?.get() == null){
        val component = DaggerMainActivityComponent.factory()
            .create(activity, CoreComponent.coreComponent(activity.application))
        activityComponent = WeakReference(component)
    }
    return activityComponent!!.get()!!
}

internal fun MainActivity.inject() {
    buildComponent(this).inject(this)
}

internal fun CustomBottomNavigator.inject(){
    buildComponent(context as MainActivity).inject(this)
}

internal fun MainActivity.clearComponent(){
    activityComponent?.clear()
    activityComponent = null
}

@Component(
    modules = arrayOf(
        PresentationModelModule::class,

        AndroidInjectionModule::class,
        ViewModelModule::class,
        MainActivityModule::class,
        MainActivityFragmentsModule::class,
//
//        // fragments
        TabFragmentInjector::class,
        FolderTreeFragmentModule::class,
        DetailFragmentInjector::class,
        PlayerFragmentModule::class,
        RecentlyAddedFragmentInjector::class,
        RelatedArtistFragmentInjector::class,
        SearchFragmentInjector::class,
        PlayingQueueFragmentInjector::class,
        CreatePlaylistFragmentInjector::class,
        EqualizerModule::class,

        SettingsFragmentsModule::class,

        EditItemModule::class,
        AboutFragmentModule::class,

        DialogModule::class
    ), dependencies = [CoreComponent::class]
)
@PerActivity
internal interface MainActivityComponent {

    fun inject(instance: MainActivity)
    fun inject(bottomNavigation: CustomBottomNavigator)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance instance: MainActivity, component: CoreComponent): MainActivityComponent
    }

}