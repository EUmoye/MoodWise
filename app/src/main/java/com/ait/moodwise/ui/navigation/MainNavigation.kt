package com.ait.moodwise.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ait.moodwise.MainActivity
import com.ait.moodwise.ui.screens.MapAnimation
import com.ait.moodwise.ui.screens.WeatherDetails
//import com.ait.moodwise.ui.screens.WeatherDetails
import com.ait.moodwise.ui.screens.WeatherDetailsContent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    navController: NavHostController
) {
    val currentTime = remember { mutableStateOf(System.currentTimeMillis()) }


    NavHost(
        navController = navController,
        startDestination = "Home"
    ) {
        // Weather Details Screen
        composable(route = "Weather") {
//            WeatherDetailsContent(
////                weather = "weather",
////                recommendedActivities = listOf("Swimming", "Beach Volleyball", "Hiking"),
//                currentTime = currentTime.value
//            )
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