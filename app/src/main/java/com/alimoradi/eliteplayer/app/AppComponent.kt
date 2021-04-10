package com.alimoradi.eliteplayer.app

import dagger.Component
import dagger.android.AndroidInjectionModule
import com.alimoradi.injection.CoreComponent
import com.alimoradi.eliteplayer.appwidgets.WidgetBindingModule
import javax.inject.Scope

@Component(
    modules = [
        AndroidInjectionModule::class,
        WidgetBindingModule::class
    ], dependencies = [CoreComponent::class]
)
@PerApp
interface AppComponent {

    fun inject(instance: App)

    @Component.Factory
    interface Factory {
        fun create(component: CoreComponent): AppComponent
    }

}

@Scope
annotation class PerApp