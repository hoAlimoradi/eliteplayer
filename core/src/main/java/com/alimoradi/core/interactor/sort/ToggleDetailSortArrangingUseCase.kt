package com.alimoradi.core.interactor.sort

import com.alimoradi.core.MediaIdCategory
import com.alimoradi.core.prefs.SortPreferences
import javax.inject.Inject

class ToggleDetailSortArrangingUseCase @Inject constructor(
    private val gateway: SortPreferences

) {

    operator fun invoke(mediaIdCategory: MediaIdCategory) {
        return gateway.toggleDetailSortArranging(mediaIdCategory)
    }
}