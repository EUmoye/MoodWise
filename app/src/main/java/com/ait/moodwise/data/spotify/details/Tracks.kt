package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tracks(
    @SerialName("href")
    var href: String = "",
    @SerialName("total")
    var total: Int = 0
)