package com.alimoradi.presentation.widgets.bottomnavigator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.alimoradi.analytics.TrackerFacade
import com.alimoradi.presentation.library.LibraryFragment
import com.alimoradi.presentation.queue.PlayingQueueFragment
import com.alimoradi.presentation.search.SearchFragment
import com.alimoradi.sharedandroid.extensions.fragmentTransaction
import com.alimoradi.presentation.R
import com.alimoradi.presentation.model.BottomNavigationPage
import com.alimoradi.presentation.model.LibraryPage

internal class BottomNavigator {

    private val tags = listOf(
            LibraryFragment.TAG_TRACK,
            LibraryFragment.TAG_PODCAST,
            SearchFragment.TAG,
            PlayingQueueFragment.TAG
    )

    fun navigate(
        activity: FragmentActivity,
        trackerFacade: TrackerFacade,
        page: BottomNavigationPage,
        libraryPage: LibraryPage
    ) {
        val fragmentTag = page.toFragmentTag(libraryPage)

        if (!tags.contains(fragmentTag)) {
            throw IllegalArgumentException("invalid fragment tag $fragmentTag")
        }

        for (index in 0..activity.supportFragmentManager.backStackEntryCount) {
            // clear the backstack
            activity.supportFragmentManager.popBackStack()
        }

        activity.fragmentTransaction {
            disallowAddToBackStack()
            setReorderingAllowed(true)
            // hide other categories fragment
            hidesAllBottomNavigationFragments(activity)

            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

            val fragment = activity.supportFragmentManager.findFragmentByTag(fragmentTag)
            if (fragment == null) {
                val newFragment = tagToInstance(fragmentTag)
                trackerFacade.trackScreen(newFragment::class.java.simpleName, newFragment.arguments)
                add(R.id.fragmentContainer, newFragment, fragmentTag)
            } else {
                trackerFacade.trackScreen(fragment::class.java.simpleName, fragment.arguments)
                show(fragment)
            }
        }
    }

    private fun FragmentTransaction.hidesAllBottomNavigationFragments(activity: FragmentActivity){
        activity.supportFragmentManager.fragments
                .asSequence()
                .filter { tags.contains(it.tag) }
                .forEach { hide(it) }
    }

    private fun BottomNavigationPage.toFragmentTag(libraryPage: LibraryPage): String {
        return when (this){
            BottomNavigationPage.LIBRARY -> {
                when(libraryPage){
                    LibraryPage.TRACKS -> LibraryFragment.TAG_TRACK
                    LibraryPage.PODCASTS -> LibraryFragment.TAG_PODCAST
                }
            }
            BottomNavigationPage.SEARCH -> SearchFragment.TAG
            BottomNavigationPage.QUEUE -> PlayingQueueFragment.TAG
        }
    }

    private fun tagToInstance(tag: String): Fragment = when (tag) {
        LibraryFragment.TAG_TRACK -> LibraryFragment.newInstance(false)
        LibraryFragment.TAG_PODCAST -> LibraryFragment.newInstance(true)
        SearchFragment.TAG -> SearchFragment()
        PlayingQueueFragment.TAG -> PlayingQueueFragment()
        else -> throw IllegalArgumentException("invalid fragment tag $tag")
    }
}