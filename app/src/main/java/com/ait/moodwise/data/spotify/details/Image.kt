package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    @SerialName("height")
    var height: Int = 0,
    @SerialName("url")
    var url: String = "",
    @SerialName("width")
    var width: Int = 0
)