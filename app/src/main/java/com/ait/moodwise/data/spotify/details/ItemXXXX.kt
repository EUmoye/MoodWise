package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemXXXX(
    @SerialName("collaborative")
    var collaborative: Boolean = false,
    @SerialName("description")
    var description: String = "",
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
    @SerialName("owner")
    var owner: Owner = Owner(),
    @SerialName("public")
    var `public`: Boolean = false,
    @SerialName("snapshot_id")
    var snapshotId: String = "",
    @SerialName("tracks")
    var tracks: Tracks = Tracks(),
    @SerialName("type")
    var type: String = "",
    @SerialName("uri")
    var uri: String = ""
)