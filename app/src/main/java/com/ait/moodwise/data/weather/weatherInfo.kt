package com.ait.moodwise.data.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class weatherInfo(
    @SerialName("base")
    var base: String = "",
    @SerialName("clouds")
    var clouds: Clouds = Clouds(),
    @SerialName("cod")
    var cod: Int = 0,
    @SerialName("coord")
    var coord: Coord = Coord(),
    @SerialName("dt")
    var dt: Int = 0,
    @SerialName("id")
    var id: Int = 0,
    @SerialName("main")
    var main: Main = Main(),
    @SerialName("name")
    var name: String = "",
    @SerialName("sys")
    var sys: Sys = Sys(),
    @SerialName("timezone")
    var timezone: Int = 0,
    @SerialName("visibility")
    var visibility: Int = 0,
    @SerialName("weather")
    var weather: List<Weather> = listOf(),
    @SerialName("wind")
    var wind: Wind = Wind()
)