package com.ait.moodwise.data.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    @SerialName("lat")
    var lat: Double = 0.0,
    @SerialName("lon")
    var lon: Double = 0.0
)