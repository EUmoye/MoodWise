package com.ait.moodwise.data.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Main(
    @SerialName("feels_like")
    var feelsLike: Double = 0.0,
    @SerialName("grnd_level")
    var grndLevel: Int = 0,
    @SerialName("humidity")
    var humidity: Int = 0,
    @SerialName("pressure")
    var pressure: Int = 0,
    @SerialName("sea_level")
    var seaLevel: Int = 0,
    @SerialName("temp")
    var temp: Double = 0.0,
    @SerialName("temp_max")
    var tempMax: Double = 0.0,
    @SerialName("temp_min")
    var tempMin: Double = 0.0
)