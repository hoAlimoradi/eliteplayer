package com.alimoradi.presentation.main

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.alimoradi.appshortcuts.Shortcuts
import dev.olog.core.MediaId
import com.alimoradi.intents.AppConstants
import com.alimoradi.intents.Classes
import com.alimoradi.intents.FloatingWindowsConstants
import com.alimoradi.intents.MusicServiceAction
import com.alimoradi.presentation.FloatingWindowHelper
import com.alimoradi.presentation.R
import com.alimoradi.presentation.folder.tree.FolderTreeFragment
import com.alimoradi.presentation.interfaces.*
import com.alimoradi.presentation.library.LibraryFragment
import com.alimoradi.presentation.main.di.clearComponent
import com.alimoradi.presentation.main.di.inject
import com.alimoradi.presentation.model.BottomNavigationPage
import com.alimoradi.presentation.model.PresentationPreferencesGateway
import com.alimoradi.presentation.navigator.Navigator
import com.alimoradi.presentation.pro.HasBilling
import com.alimoradi.presentation.pro.IBilling
import com.alimoradi.presentation.rateapp.RateAppDialog
import com.alimoradi.presentation.utils.collapse
import com.alimoradi.presentation.utils.expand
import com.alimoradi.presentation.utils.isExpanded
import dev.olog.scrollhelper.MultiListenerBottomSheetBehavior
import dev.olog.scrollhelper.ScrollType
import com.alimoradi.sharedandroid.extensions.*
import com.alimoradi.sharedandroid.theme.hasPlayerAppearance
import com.alimoradi.sharedandroid.theme.isImmersiveMode
import com.alimoradi.shared.lazyFast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_navigation.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : MusicGlueActivity(),
    HasSlidingPanel,
    HasBilling,
    HasBottomNavigation,
    OnPermissionChanged {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel by lazyFast {
        viewModelProvider<MainActivityViewModel>(
            factory
        )
    }
    @Inject
    lateinit var navigator: Navigator
    // handles lifecycle itself
    @Inject
    override lateinit var billing: IBilling

    @Inject
    internal lateinit var presentationPrefs: PresentationPreferencesGateway

    @Suppress("unused")
    @Inject
    lateinit var statusBarColorBehavior: StatusBarColorBehavior
    @Suppress("unused")
    @Inject
    lateinit var rateAppDialog: RateAppDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (isImmersiveMode()){
            // workaround, on some device on immersive mode bottom navigation disappears
            rootView.fitsSystemWindows = true
            slidingPanel.fitsSystemWindows = true
            bottomWrapper.fitsSystemWindows = true
        }

        if (hasPlayerAppearance().isMini()){
            // TODO made a resource value
            slidingPanelFade.parallax = 0
            slidingPanel.setHeight(dip(300))
        }

        setupSlidingPanel()

        when {
            viewModel.isFirstAccess() -> {
                navigator.toFirstAccess()
                return
            }
            savedInstanceState == null -> navigateToLastPage()
        }

        intent?.let { handleIntent(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearComponent()
    }

    override fun onPermissionGranted(permission: Permission) = when (permission){
        Permission.STORAGE -> {
            navigateToLastPage()
            connect()
        }
    }

    private fun setupSlidingPanel(){
        if (!isTablet) {
            val scrollHelper = SuperCerealScrollHelper(
                this, ScrollType.Full(
                    slidingPanel = slidingPanel,
                    bottomNavigation = bottomWrapper,
                    toolbarHeight = dimen(R.dimen.toolbar),
                    tabLayoutHeight = dimen(R.dimen.tab),
                    realSlidingPanelPeek = dimen(R.dimen.sliding_panel_peek)
                )
            )
            lifecycle.addObserver(scrollHelper)
        }
    }

    private fun navigateToLastPage(){
        bottomNavigation.navigateToLastPage()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(it) }
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            FloatingWindowsConstants.ACTION_START_SERVICE -> {
                FloatingWindowHelper.startServiceIfHasOverlayPermission(this)
            }
            Shortcuts.SEARCH -> bottomNavigation.navigate(BottomNavigationPage.SEARCH)
            AppConstants.ACTION_CONTENT_VIEW -> getSlidingPanel().expand()
            MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH -> {
                val serviceIntent = Intent(this, Class.forName(Classes.SERVICE_MUSIC))
                serviceIntent.action = MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH
                ContextCompat.startForegroundService(this, serviceIntent)
            }
            Shortcuts.DETAIL -> {
                launch {
                    delay(250)
                    val string = intent.getStringExtra(Shortcuts.DETAIL_EXTRA_ID)!!
                    val mediaId = MediaId.fromString(string)
                    navigator.toDetailFragment(mediaId)
                }
            }
            Intent.ACTION_VIEW -> {
                val serviceIntent = Intent(this, Class.forName(Classes.SERVICE_MUSIC))
                serviceIntent.action = MusicServiceAction.PLAY_URI.name
                serviceIntent.data = intent.data
                ContextCompat.startForegroundService(this, serviceIntent)
            }
        }
        intent.action = null
        setIntent(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FloatingWindowHelper.REQUEST_CODE_HOVER_PERMISSION) {
            FloatingWindowHelper.startServiceIfHasOverlayPermission(this)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        try {
            val topFragment = supportFragmentManager.getTopFragment()

            when {
                topFragment is CanHandleOnBackPressed && topFragment.handleOnBackPressed()-> {
                    return
                }
                topFragment is DrawsOnTop -> {
                    super.onBackPressed()
                    return
                }
                getSlidingPanel().isExpanded() -> {
                    getSlidingPanel().collapse()
                    return
                }
            }
            if (tryPopFolderBack()) {
                return
            }

            super.onBackPressed()
        } catch (ex: IllegalStateException) {
            /*random fragment manager crashes */
            ex.printStackTrace()
        }

    }

    private fun tryPopFolderBack(): Boolean {
        val categoriesFragment =
            supportFragmentManager.findFragmentByTag(LibraryFragment.TAG_TRACK) as? LibraryFragment ?: return false

        if (categoriesFragment.isCurrentFragmentFolderTree()){
            val folderTree = categoriesFragment.childFragmentManager.fragments
                .find { it is FolderTreeFragment } as? CanHandleOnBackPressed
            return folderTree?.handleOnBackPressed() == true
        }
        return false
    }

    override fun getSlidingPanel(): MultiListenerBottomSheetBehavior<*> {
        return BottomSheetBehavior.from(slidingPanel) as MultiListenerBottomSheetBehavior<*>
    }

    override fun navigate(page: BottomNavigationPage) {
        bottomNavigation.navigate(page)
    }

    fun restoreUpperWidgetsTranslation(){
        findViewById<View>(R.id.toolbar)?.animate()?.translationY(0f)
        findViewById<View>(R.id.tabLayout)?.animate()?.translationY(0f)
    }
}