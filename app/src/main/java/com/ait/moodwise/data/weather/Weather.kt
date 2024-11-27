package com.ait.moodwise.data.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    @SerialName("description")
    var description: String = "",
    @SerialName("icon")
    var icon: String = "",
    @SerialName("id")
    var id: Int = 0,
    @SerialName("main")
    var main: String = ""
)