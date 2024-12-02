package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExternalIds(
    @SerialName("ean")
    var ean: String = "",
    @SerialName("isrc")
    var isrc: String = "",
    @SerialName("upc")
    var upc: String = ""
)