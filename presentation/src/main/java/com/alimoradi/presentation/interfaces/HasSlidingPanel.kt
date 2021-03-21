package com.alimoradi.presentation.interfaces

import dev.olog.scrollhelper.MultiListenerBottomSheetBehavior

interface HasSlidingPanel {

    fun getSlidingPanel(): MultiListenerBottomSheetBehavior<*>

}
