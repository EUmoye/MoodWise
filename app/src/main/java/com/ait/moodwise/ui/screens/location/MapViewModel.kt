package com.ait.moodwise.ui.screens.location

import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ait.moodwise.data.activity.ActivitiesItem
import com.ait.moodwise.data.weather.weatherInfo
import com.ait.moodwise.location.LocationManager
import com.ait.moodwise.ui.screens.GenAIViewModel
import com.ait.moodwise.ui.screens.WeatherDetailsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.os.Build
import java.util.Locale


@HiltViewModel
class MapViewModel @Inject constructor(
    val locationManager: LocationManager,
) : ViewModel() {
    val weatherInfo = mutableStateOf<weatherInfo?>(null)
    val activitiesList = mutableStateOf<List<ActivitiesItem>>(emptyList())
    val isLoading = mutableStateOf(true)
    var locationState = mutableStateOf<Location?>(null)

    fun startLocationMonitoring() {
        viewModelScope.launch {
            locationManager
                .fetchUpdates()
                .collect { location ->
                    locationState.value = location
                }
        }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun fetchWeatherAndActivities(location: Location, context: Context) {
//        viewModelScope.launch {
//            val geocoder = Geocoder(context, Locale.getDefault())
//            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
//            val cityCountry = addresses?.firstOrNull()?.let { address ->
//                "${address.locality},${address.countryCode}"
//            } ?: "New York,US"
//
//            weatherDetailsViewModel.getWeatherDetails(
//                cityCountry = cityCountry,
//                apiKey = "3b3fb7a3dda7a0a2788ae82328224214"
//            )
//            weatherDetailsViewModel.startPeriodicUpdates(
//                cityCountry = cityCountry,
//                apiKey = "3b3fb7a3dda7a0a2788ae82328224214"
//            )
//
//            activitiesList.value = genAIViewModel.json_no_schema(cityCountry)
//            weatherInfo.value = weatherDetailsViewModel.weatherUIState.value.weatherInfo
//            isLoading.value = false
//        }
//    }
}