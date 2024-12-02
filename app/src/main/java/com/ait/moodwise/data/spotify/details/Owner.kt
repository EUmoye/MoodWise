package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    @SerialName("display_name")
    var displayName: String = "",
    @SerialName("external_urls")
    var externalUrls: ExternalUrlsX = ExternalUrlsX(),
    @SerialName("followers")
    var followers: Followers = Followers(),
    @SerialName("href")
    var href: String = "",
    @SerialName("id")
    var id: String = "",
    @SerialName("type")
    var type: String = "",
    @SerialName("uri")
    var uri: String = ""
)