package com.ait.moodwise.data.spotify.details


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class spotifyDetails(
    @SerialName("albums")
    var albums: Albums = Albums(),
    @SerialName("artists")
    var artists: Artists = Artists(),
    @SerialName("audiobooks")
    var audiobooks: Audiobooks = Audiobooks(),
    @SerialName("episodes")
    var episodes: Episodes = Episodes(),
    @SerialName("playlists")
    var playlists: Playlists = Playlists(),
    @SerialName("shows")
    var shows: Shows = Shows(),
    @SerialName("tracks")
    var tracks: TracksX = TracksX()
)