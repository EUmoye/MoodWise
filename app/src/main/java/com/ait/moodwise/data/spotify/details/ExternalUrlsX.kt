package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExternalUrlsX(
    @SerialName("spotify")
    var spotify: String = ""
)