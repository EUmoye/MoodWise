package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemXX(
    @SerialName("authors")
    var authors: List<Author> = listOf(),
    @SerialName("available_markets")
    var availableMarkets: List<String> = listOf(),
    @SerialName("copyrights")
    var copyrights: List<Copyright> = listOf(),
    @SerialName("description")
    var description: String = "",
    @SerialName("edition")
    var edition: String = "",
    @SerialName("explicit")
    var explicit: Boolean = false,
    @SerialName("external_urls")
    var externalUrls: ExternalUrlsX = ExternalUrlsX(),
    @SerialName("href")
    var href: String = "",
    @SerialName("html_description")
    var htmlDescription: String = "",
    @SerialName("id")
    var id: String = "",
    @SerialName("images")
    var images: List<Image> = listOf(),
    @SerialName("languages")
    var languages: List<String> = listOf(),
    @SerialName("media_type")
    var mediaType: String = "",
    @SerialName("name")
    var name: String = "",
    @SerialName("narrators")
    var narrators: List<Narrator> = listOf(),
    @SerialName("publisher")
    var publisher: String = "",
    @SerialName("total_chapters")
    var totalChapters: Int = 0,
    @SerialName("type")
    var type: String = "",
    @SerialName("uri")
    var uri: String = ""
)