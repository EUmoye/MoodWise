package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Author(
    @SerialName("name")
    var name: String = ""
)