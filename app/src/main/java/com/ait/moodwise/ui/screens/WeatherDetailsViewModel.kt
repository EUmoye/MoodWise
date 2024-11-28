package com.ait.moodwise.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ait.moodwise.data.weather.weatherInfo
import com.ait.moodwise.network.WeatherAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class WeatherDetailsViewModel @Inject constructor(
    private val weatherAPI: WeatherAPI
) : ViewModel() {

    var weatherUIState: WeatherUIState by mutableStateOf(WeatherUIState.Init)

    fun getWeatherDetails(cityCountry: String, apiKey: String) {
        weatherUIState = WeatherUIState.Loading
        Log.d("WeatherDetailsViewModel", "Fetching weather details...") // Log added for debugging
        viewModelScope.launch {
            weatherUIState = try {
                val response = weatherAPI.getWeather(cityCountry, "metric", apiKey)
                WeatherUIState.Success(response)
            } catch (e: IOException) {
                WeatherUIState.Error
            } catch (e: HttpException) {
                WeatherUIState.Error
            } catch (e: Exception) {
                WeatherUIState.Error
            }
        }
    }
}



sealed interface WeatherUIState {
    object Init : WeatherUIState
    object Loading : WeatherUIState
    data class Success(val weatherInfo: weatherInfo) : WeatherUIState
    object Error : WeatherUIState
}


//// Generate recommended activities based on weather conditions
//private fun generateRecommendedActivities(weatherInfo: weatherInfo): List<String> {
//    return when {
//        weatherInfo.main.temp > 25 -> listOf("Swimming", "Beach Volleyball", "Hiking")
//        weatherInfo.main.temp in 15..25 -> listOf("Jogging", "Cycling", "Picnic")
//        weatherInfo.main.temp < 15 -> listOf("Indoor Yoga", "Reading", "Cooking")
//        else -> listOf("Relaxing", "Meditation")
//    }
//}