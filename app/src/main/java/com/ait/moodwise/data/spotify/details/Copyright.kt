package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Copyright(
    @SerialName("text")
    var text: String = "",
    @SerialName("type")
    var type: String = ""
)