package com.alimoradi.presentation.main

import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.alimoradi.presentation.BuildConfig
import com.alimoradi.presentation.R
import com.alimoradi.presentation.about.AboutFragment
import com.alimoradi.presentation.detail.DetailFragment
import com.alimoradi.presentation.tree.FolderTreeFragment
import com.alimoradi.presentation.library.LibraryFragment
import com.alimoradi.presentation.prefs.SettingsFragment
import com.alimoradi.presentation.queue.PlayingQueueFragment
import dev.olog.scrollhelper.ScrollHelper
import dev.olog.scrollhelper.ScrollType
import com.alimoradi.sharedandroid.extensions.findViewByIdNotRecursive
import com.alimoradi.sharedandroid.extensions.setMargin

class SuperCerealScrollHelper(
    activity: FragmentActivity,
    input: ScrollType
) : ScrollHelper(activity, input, false, false, false) { // debug is very slow

    override fun applyInsetsToList(fragment: Fragment, list: RecyclerView, toolbar: View?, tabLayout: View?) {
        super.applyInsetsToList(fragment, list, toolbar, tabLayout)
        if (fragment.tag?.startsWith(DetailFragment.TAG) == true){
            // apply only top padding
            list.updatePadding(top = 0)
        }
        if (fragment is FolderTreeFragment){
            val crumbsWrapper = fragment.view!!.findViewById<View>(R.id.crumbsWrapper)
            if (crumbsWrapper.marginTop < 1){
//                 margin not set yet
                fragment.view!!.doOnPreDraw {
                    crumbsWrapper.setMargin(top = toolbar!!.height + tabLayout!!.height)
                    list.updatePadding(top = list.paddingTop + crumbsWrapper!!.height)
                }
            }
        }
    }

    override fun searchForFab(fragment: Fragment): View? {
        return fragment.view?.findViewById(R.id.fab)
    }

    override fun searchForRecyclerView(fragment: Fragment): RecyclerView? {
        var recyclerView = fragment.view?.findViewByIdNotRecursive<RecyclerView>(R.id.list)
        if (recyclerView == null && fragment.tag == SettingsFragment.TAG) {
            recyclerView = fragment.view?.findViewById(R.id.recycler_view)
        }
        return recyclerView
    }

    override fun searchForTabLayout(fragment: Fragment): View? {
        val view : View? = when {
            isViewPagerChildTag(fragment.tag) -> {
                // search toolbar and tab layout in parent fragment
                fragment.parentFragment?.view
            }
            else -> fragment.view
        }
        return view?.findViewByIdNotRecursive(R.id.tabLayout)
    }

    override fun searchForToolbar(fragment: Fragment): View? {
        if (fragment.tag == PlayingQueueFragment.TAG){
            // for some reason when drag and drop in queue fragment, the queue became crazy
            return null
        }
        val view : View? = when {
            isViewPagerChildTag(fragment.tag) -> {
                // search toolbar and tab layout in parent fragment
                fragment.parentFragment?.view
            }
            fragment.tag == SettingsFragment.TAG -> fragment.parentFragment?.view
            else -> fragment.view
        }
        return view?.findViewByIdNotRecursive(R.id.toolbar)
    }

    override fun searchForViewPager(fragment: Fragment): ViewPager? {
        val tag = fragment.tag
        if (tag == LibraryFragment.TAG_TRACK || tag == LibraryFragment.TAG_PODCAST){
            return fragment.view?.findViewByIdNotRecursive(R.id.viewPager)
        }
        return null
    }

    override fun skipFragment(fragment: Fragment): Boolean {
        if (isViewPagerChildTag(fragment.tag)){
            return false
        }
        return isPlayerTag(fragment.tag) || !hasFragmentOwnership(fragment.tag)
    }

    private fun isViewPagerChildTag(tag: String?) = tag?.startsWith("android:switcher:") == true

    private fun hasFragmentOwnership(tag: String?) = tag?.startsWith("dev.olog") == true

    private fun isPlayerTag(tag: String?) = tag?.contains("Player") == true
}