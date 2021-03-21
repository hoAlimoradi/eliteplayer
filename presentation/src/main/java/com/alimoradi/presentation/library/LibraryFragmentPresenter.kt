package com.alimoradi.presentation.library

import com.alimoradi.presentation.model.LibraryCategoryBehavior
import com.alimoradi.presentation.model.PresentationPreferencesGateway
import dev.olog.core.prefs.TutorialPreferenceGateway
import com.alimoradi.presentation.model.LibraryPage
import com.alimoradi.shared.clamp
import javax.inject.Inject

internal class LibraryFragmentPresenter @Inject constructor(
    private val appPrefsUseCase: PresentationPreferencesGateway,
    private val tutorialPreferenceUseCase: TutorialPreferenceGateway
) {

    fun getViewPagerLastPage(totalPages: Int, isPodcast: Boolean): Int {
        val lastPage = if (isPodcast) {
            appPrefsUseCase.getViewPagerPodcastLastPage()
        } else {
            appPrefsUseCase.getViewPagerLibraryLastPage()
        }
        return clamp(lastPage, 0, totalPages)
    }

    fun setViewPagerLastPage(page: Int, isPodcast: Boolean) {
        if (isPodcast) {
            appPrefsUseCase.setViewPagerPodcastLastPage(page)
        } else {
            appPrefsUseCase.setViewPagerLibraryLastPage(page)
        }
    }

    fun showFloatingWindowTutorialIfNeverShown(): Boolean {
        return tutorialPreferenceUseCase.floatingWindowTutorial()
    }

    fun getCategories(isPodcast: Boolean): List<LibraryCategoryBehavior> {
        if (isPodcast) {
            return appPrefsUseCase.getPodcastLibraryCategories()
                .filter { it.visible }
        }
        return appPrefsUseCase.getLibraryCategories()
            .filter { it.visible }
    }

    fun setLibraryPage(page: LibraryPage) {
        appPrefsUseCase.setLibraryPage(page)
    }

    fun canShowPodcasts() = appPrefsUseCase.canShowPodcasts()

}