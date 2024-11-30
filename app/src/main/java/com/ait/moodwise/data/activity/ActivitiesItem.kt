package com.ait.moodwise.data.activity


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivitiesItem(
    @SerialName("description")
    var description: String = "",
    @SerialName("location")
    var location: String = "",
    @SerialName("name")
    var name: String = ""
)