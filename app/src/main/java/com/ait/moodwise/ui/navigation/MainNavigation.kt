package com.ait.moodwise.ui.navigation

sealed class MainNavigation(val route: String) {
    object MainScreen : MainNavigation("mainscreen")

    object MoodTracker : MainNavigation("moodtracker")

}