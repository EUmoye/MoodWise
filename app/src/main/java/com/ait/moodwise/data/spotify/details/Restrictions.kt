package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Restrictions(
    @SerialName("reason")
    var reason: String = ""
)