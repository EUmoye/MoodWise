package com.ait.moodwise.data.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sys(
    @SerialName("country")
    var country: String = "",
    @SerialName("id")
    var id: Int = 0,
    @SerialName("sunrise")
    var sunrise: Int = 0,
    @SerialName("sunset")
    var sunset: Int = 0,
    @SerialName("type")
    var type: Int = 0
)