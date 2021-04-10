package com.alimoradi.presentation.interfaces

import com.alimoradi.presentation.model.BottomNavigationPage

interface HasBottomNavigation {
    fun navigate(page: BottomNavigationPage)
}