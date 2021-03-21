package com.alimoradi.equalizer

import android.os.Build
import dagger.Binds
import dagger.Lazy
import dagger.Module
import dagger.Provides
import com.alimoradi.equalizer.bassboost.BassBoostImpl
import com.alimoradi.equalizer.bassboost.BassBoostProxy
import com.alimoradi.equalizer.bassboost.IBassBoost
import com.alimoradi.equalizer.bassboost.IBassBoostInternal
import com.alimoradi.equalizer.equalizer.*
import com.alimoradi.equalizer.virtualizer.IVirtualizer
import com.alimoradi.equalizer.virtualizer.IVirtualizerInternal
import com.alimoradi.equalizer.virtualizer.VirtualizerImpl
import com.alimoradi.equalizer.virtualizer.VirtualizerProxy
import javax.inject.Singleton

@Module
abstract class EqualizerModule {

    // proxies

    @Binds
    @Singleton
    internal abstract fun provideEqualizer(impl: EqualizerProxy): IEqualizer

    @Binds
    @Singleton
    internal abstract fun provideBassBoost(impl: BassBoostProxy): IBassBoost

    @Binds
    @Singleton
    internal abstract fun provideVirtualizer(impl: VirtualizerProxy): IVirtualizer



    // implementation

    @Binds
    internal abstract fun provideBassBoostInternal(impl: BassBoostImpl): IBassBoostInternal

    @Binds
    internal abstract fun provideVirtualizerInternal(impl: VirtualizerImpl): IVirtualizerInternal

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideInternalEqualizer(
            equalizerImpl: Lazy<EqualizerImpl>,
            equalizerImpl28: Lazy<EqualizerImpl28>
        ): IEqualizerInternal {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                try {
                    // crashes on some devices
                    return equalizerImpl28.get()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    return equalizerImpl.get()
                }
            }
            return equalizerImpl.get()
        }

    }

}