package com.ait.moodwise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ait.moodwise.MainActivity
import com.ait.moodwise.ui.screens.MapAnimation
import com.ait.moodwise.ui.screens.WeatherDetails

@Composable
fun MainNavigation(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        // Weather Details Screen
        composable(route = "Weather") {
            WeatherDetails(viewModel = hiltViewModel())
        }

        composable(route = "Home") {
            MainActivity()
        }

        // Map Animation Screen
        composable(route = "map") {
            MapAnimation()
        }
    }
}