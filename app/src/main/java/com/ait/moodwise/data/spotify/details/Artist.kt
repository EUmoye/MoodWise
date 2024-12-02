package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Artist(
    @SerialName("external_urls")
    var externalUrls: ExternalUrlsX = ExternalUrlsX(),
    @SerialName("href")
    var href: String = "",
    @SerialName("id")
    var id: String = "",
    @SerialName("name")
    var name: String = "",
    @SerialName("type")
    var type: String = "",
    @SerialName("uri")
    var uri: String = ""
)