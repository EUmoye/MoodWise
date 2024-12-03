package com.ait.moodwise.ui.screens.location

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ait.moodwise.location.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    val locationManager: LocationManager
) : ViewModel() {

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
}