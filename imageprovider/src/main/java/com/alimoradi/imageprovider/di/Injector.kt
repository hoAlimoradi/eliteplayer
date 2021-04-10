package com.alimoradi.imageprovider.di

import android.app.Application
import android.content.Context
import com.alimoradi.imageprovider.GlideModule
import com.alimoradi.injection.CoreComponent

fun GlideModule.inject(context: Context){
    DaggerImageProviderComponent.factory()
        .create(CoreComponent.coreComponent(context.applicationContext as Application))
        .inject(this)
}