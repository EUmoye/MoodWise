package com.ait.moodwise.network

import com.ait.moodwise.data.weather.weatherInfo
import retrofit2.http.GET
import retrofit2.http.Query

// https://api.openweathermap.org/data/2.5/weather?q=Budapest,hu&units=metric&appid=3b3fb7a3dda7a0a2788ae82328224214

// Host: https://api.openweathermap.org/
// Path: /data/2.5/weather
// Query paramters: ?q=Budapest,hu&units=metric&appid=3b3fb7a3dda7a0a2788ae82328224214


interface WeatherAPI {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") cityCountry: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): weatherInfo

}