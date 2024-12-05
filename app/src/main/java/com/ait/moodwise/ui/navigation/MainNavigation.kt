package com.ait.moodwise.ui.navigation

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ait.moodwise.ui.screens.location.LocationContent
import com.ait.moodwise.ui.screens.location.MapViewModel
import com.ait.moodwise.ui.screens.location.RequestPermission
import com.ait.moodwise.ui.screens.music.MusicScreen
import com.ait.moodwise.ui.screens.weather.WeatherDetails
import com.google.accompanist.permissions.ExperimentalPermissionsApi



@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    navController: NavHostController
) {
    val currentTime = remember { mutableStateOf(System.currentTimeMillis()) }
    val mapViewModel: MapViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "Music"
    ) {
        composable(route = "Weather") {
            WeatherDetails(mapViewModel)
        }

        composable(route = "Music") {
            MusicScreen()
        }

        composable(route = "Home") {

            // Map Animation Screen
            composable(route = "map") {
                val context = LocalContext.current
                RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION) {
                    LocationContent(
                        context = context,
                        navController = navController,
                        viewModel = mapViewModel
                    )
                }
            }
        }
    }
}