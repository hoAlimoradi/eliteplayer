package com.alimoradi.servicemusic.notification

import dagger.Lazy
import dagger.Module
import dagger.Provides
import com.alimoradi.injection.dagger.PerService
import com.alimoradi.servicemusic.interfaces.INotification
import com.alimoradi.sharedandroid.utils.isNougat
import com.alimoradi.sharedandroid.utils.isOreo

@Module
internal object NotificationModule {

    @Provides
    @PerService
    @JvmStatic
    internal fun provideNotificationImpl(
        notificationImpl26: Lazy<NotificationImpl26>,
        notificationImpl24: Lazy<NotificationImpl24>,
        notificationImpl: Lazy<NotificationImpl21>

    ): INotification {
        return when {
            isOreo() -> notificationImpl26.get()
            isNougat() -> notificationImpl24.get()
            else -> notificationImpl.get()
        }
    }

}