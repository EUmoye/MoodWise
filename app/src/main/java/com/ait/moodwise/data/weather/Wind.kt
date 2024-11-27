package com.ait.moodwise.data.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    @SerialName("deg")
    var deg: Int = 0,
    @SerialName("speed")
    var speed: Double = 0.0
)