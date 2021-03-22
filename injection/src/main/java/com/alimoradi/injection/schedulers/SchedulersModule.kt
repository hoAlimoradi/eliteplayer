package com.alimoradi.injection.schedulers

import dagger.Binds
import dagger.Module
import com.alimoradi.core.schedulers.Schedulers

@Module
abstract class SchedulersModule {

    @Binds
    internal abstract fun provideSchedulers(impl: SchedulersProd): Schedulers

}