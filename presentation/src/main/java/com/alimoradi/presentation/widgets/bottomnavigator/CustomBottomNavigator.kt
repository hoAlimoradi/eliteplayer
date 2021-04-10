package com.alimoradi.presentation.widgets.bottomnavigator

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.alimoradi.analytics.TrackerFacade
import com.alimoradi.presentation.R
import com.alimoradi.presentation.main.di.inject
import com.alimoradi.presentation.model.BottomNavigationPage
import com.alimoradi.presentation.model.PresentationPreferencesGateway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class CustomBottomNavigator(
        context: Context,
        attrs: AttributeSet
) : BottomNavigationView(context, attrs), CoroutineScope by MainScope() {

    @Inject
    internal lateinit var presentationPrefs: PresentationPreferencesGateway

    @Inject
    internal lateinit var trackerFacade: TrackerFacade

    private val navigator = BottomNavigator()

    init {
        inject()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val lastLibraryPage = presentationPrefs.getLastBottomViewPage()
        selectedItemId = lastLibraryPage.toMenuId()

        setOnNavigationItemSelectedListener { menu ->
            val navigationPage = menu.itemId.toBottomNavigationPage()
            val libraryPage = presentationPrefs.getLastLibraryPage()
            saveLastPage(navigationPage)
            navigator.navigate(context as FragmentActivity, trackerFacade, navigationPage, libraryPage)
            true
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setOnNavigationItemSelectedListener(null)
    }

    fun navigate(page: BottomNavigationPage) {
        selectedItemId = page.toMenuId()
    }

    fun navigateToLastPage(){
        val navigationPage = presentationPrefs.getLastBottomViewPage()
        val libraryPage = presentationPrefs.getLastLibraryPage()
        navigator.navigate(context as FragmentActivity, trackerFacade, navigationPage, libraryPage)
    }

    private fun saveLastPage(page: BottomNavigationPage){

        launch(Dispatchers.Default) { presentationPrefs.setLastBottomViewPage(page) }
    }

    private fun Int.toBottomNavigationPage(): BottomNavigationPage = when (this){
        R.id.navigation_library -> BottomNavigationPage.LIBRARY
        R.id.navigation_search -> BottomNavigationPage.SEARCH
        R.id.navigation_queue -> BottomNavigationPage.QUEUE
        else -> throw IllegalArgumentException("invalid menu id")
    }

    private fun BottomNavigationPage.toMenuId(): Int = when (this){
        BottomNavigationPage.LIBRARY -> R.id.navigation_library
        BottomNavigationPage.SEARCH -> R.id.navigation_search
        BottomNavigationPage.QUEUE -> R.id.navigation_queue
    }

}

