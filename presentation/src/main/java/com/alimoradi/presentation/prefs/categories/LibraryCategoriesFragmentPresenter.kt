package com.alimoradi.presentation.prefs.categories

import dev.olog.core.MediaIdCategory
import com.alimoradi.presentation.model.LibraryCategoryBehavior
import com.alimoradi.presentation.model.PresentationPreferencesGateway
import javax.inject.Inject

internal class LibraryCategoriesFragmentPresenter @Inject constructor(
        private val appPreferencesUseCase: PresentationPreferencesGateway
){

    fun getDefaultDataSet(category: MediaIdCategory): List<LibraryCategoryBehavior>{
        if (category == MediaIdCategory.PODCASTS){
            return appPreferencesUseCase.getDefaultPodcastLibraryCategories()
        }
        return appPreferencesUseCase.getDefaultLibraryCategories()
    }

    fun getDataSet(category: MediaIdCategory) : List<LibraryCategoryBehavior> {
        if (category == MediaIdCategory.PODCASTS){
            return appPreferencesUseCase.getPodcastLibraryCategories()
        }
        return appPreferencesUseCase.getLibraryCategories()
    }

    fun setDataSet(category: MediaIdCategory, list: List<LibraryCategoryBehavior>){
        if (category == MediaIdCategory.PODCASTS){
            appPreferencesUseCase.setPodcastLibraryCategories(list)
        } else {
            appPreferencesUseCase.setLibraryCategories(list)
        }

    }

}