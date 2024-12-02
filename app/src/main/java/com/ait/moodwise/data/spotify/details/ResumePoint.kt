package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResumePoint(
    @SerialName("fully_played")
    var fullyPlayed: Boolean = false,
    @SerialName("resume_position_ms")
    var resumePositionMs: Int = 0
)