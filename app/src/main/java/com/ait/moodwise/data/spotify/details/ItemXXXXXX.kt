package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemXXXXXX(
    @SerialName("album")
    var album: Album = Album(),
    @SerialName("artists")
    var artists: List<Artist> = listOf(),
    @SerialName("available_markets")
    var availableMarkets: List<String> = listOf(),
    @SerialName("disc_number")
    var discNumber: Int = 0,
    @SerialName("duration_ms")
    var durationMs: Int = 0,
    @SerialName("explicit")
    var explicit: Boolean = false,
    @SerialName("external_ids")
    var externalIds: ExternalIds = ExternalIds(),
    @SerialName("external_urls")
    var externalUrls: ExternalUrlsX = ExternalUrlsX(),
    @SerialName("href")
    var href: String = "",
    @SerialName("id")
    var id: String = "",
    @SerialName("is_local")
    var isLocal: Boolean = false,
    @SerialName("is_playable")
    var isPlayable: Boolean = false,
    @SerialName("linked_from")
    var linkedFrom: LinkedFrom = LinkedFrom(),
    @SerialName("name")
    var name: String = "",
    @SerialName("popularity")
    var popularity: Int = 0,
    @SerialName("preview_url")
    var previewUrl: String = "",
    @SerialName("restrictions")
    var restrictions: Restrictions = Restrictions(),
    @SerialName("track_number")
    var trackNumber: Int = 0,
    @SerialName("type")
    var type: String = "",
    @SerialName("uri")
    var uri: String = ""
)