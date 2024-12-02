package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemX(
    @SerialName("external_urls")
    var externalUrls: ExternalUrlsX = ExternalUrlsX(),
    @SerialName("followers")
    var followers: Followers = Followers(),
    @SerialName("genres")
    var genres: List<String> = listOf(),
    @SerialName("href")
    var href: String = "",
    @SerialName("id")
    var id: String = "",
    @SerialName("images")
    var images: List<Image> = listOf(),
    @SerialName("name")
    var name: String = "",
    @SerialName("popularity")
    var popularity: Int = 0,
    @SerialName("type")
    var type: String = "",
    @SerialName("uri")
    var uri: String = ""
)