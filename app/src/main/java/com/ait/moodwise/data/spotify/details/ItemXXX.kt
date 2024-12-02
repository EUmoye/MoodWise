package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemXXX(
    @SerialName("audio_preview_url")
    var audioPreviewUrl: String = "",
    @SerialName("description")
    var description: String = "",
    @SerialName("duration_ms")
    var durationMs: Int = 0,
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
    @SerialName("is_externally_hosted")
    var isExternallyHosted: Boolean = false,
    @SerialName("is_playable")
    var isPlayable: Boolean = false,
    @SerialName("language")
    var language: String = "",
    @SerialName("languages")
    var languages: List<String> = listOf(),
    @SerialName("name")
    var name: String = "",
    @SerialName("release_date")
    var releaseDate: String = "",
    @SerialName("release_date_precision")
    var releaseDatePrecision: String = "",
    @SerialName("restrictions")
    var restrictions: Restrictions = Restrictions(),
    @SerialName("resume_point")
    var resumePoint: ResumePoint = ResumePoint(),
    @SerialName("type")
    var type: String = "",
    @SerialName("uri")
    var uri: String = ""
)