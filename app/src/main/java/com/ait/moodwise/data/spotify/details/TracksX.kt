package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TracksX(
    @SerialName("href")
    var href: String = "",
    @SerialName("items")
    var items: List<ItemXXXXXX> = listOf(),
    @SerialName("limit")
    var limit: Int = 0,
    @SerialName("next")
    var next: String = "",
    @SerialName("offset")
    var offset: Int = 0,
    @SerialName("previous")
    var previous: String = "",
    @SerialName("total")
    var total: Int = 0
)