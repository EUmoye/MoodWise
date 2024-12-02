package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Album(
    @SerialName("album_type")
    var albumType: String = "",
    @SerialName("artists")
    var artists: List<Artist> = listOf(),
    @SerialName("available_markets")
    var availableMarkets: List<String> = listOf(),
    @SerialName("external_urls")
    var externalUrls: ExternalUrlsX = ExternalUrlsX(),
    @SerialName("href")
    var href: String = "",
    @SerialName("id")
    var id: String = "",
    @SerialName("images")
    var images: List<Image> = listOf(),
    @SerialName("name")
    var name: String = "",
    @SerialName("release_date")
    var releaseDate: String = "",
    @SerialName("release_date_precision")
    var releaseDatePrecision: String = "",
    @SerialName("restrictions")
    var restrictions: Restrictions = Restrictions(),
    @SerialName("total_tracks")
    var totalTracks: Int = 0,
    @SerialName("type")
    var type: String = "",
    @SerialName("uri")
    var uri: String = ""
)