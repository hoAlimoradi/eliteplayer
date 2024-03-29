package com.alimoradi.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import dagger.android.support.DaggerFragment
import com.alimoradi.presentation.interfaces.HasSlidingPanel
import com.alimoradi.presentation.main.MainActivity
import dev.olog.scrollhelper.MultiListenerBottomSheetBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseFragment : DaggerFragment(), CoroutineScope by MainScope() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(provideLayoutId(), container, false)
    }

    @LayoutRes
    protected abstract fun provideLayoutId(): Int

    fun getSlidingPanel(): MultiListenerBottomSheetBehavior<*>? {
        return (activity as HasSlidingPanel).getSlidingPanel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancel()
    }

    fun restoreUpperWidgetsTranslation(){
        (requireActivity() as MainActivity).restoreUpperWidgetsTranslation()
    }

}