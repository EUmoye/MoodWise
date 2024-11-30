package com.ait.moodwise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ait.moodwise.ui.screens.MapAnimation
import com.ait.moodwise.ui.screens.WeatherDetails
import com.ait.moodwise.ui.screens.music.MusicScreen

//sealed class MainNavigation(val route: String) {
//    object MainScreen : MainNavigation("mainscreen")
//
//    object MoodTracker : MainNavigation("moodtracker")
//
//}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "music"
    ) {
        // Weather Details Screen
        composable(route = "weather_details") {
            WeatherDetails(viewModel = hiltViewModel())
        }

        // Map Animation Screen
        composable(route = "map") {
            MapAnimation()
        }

        // Music screen
        composable(route = "music") {
            MusicScreen()
        }

    }
}