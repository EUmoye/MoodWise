package com.ait.moodwise.data.weather


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Clouds(
    @SerialName("all")
    var all: Int = 0
)