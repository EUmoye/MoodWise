package com.ait.moodwise.ui.screens.music
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MusicViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        MusicUiState(
            weather = "Rainy, 14°C",
            mood = "Relaxed",
            songs = emptyList()
        )
    )
    val uiState: StateFlow<MusicUiState> = _uiState

    init {
        // Load initial song list for default weather and mood
        loadSongsForCurrentState()
    }

    fun updateWeatherAndMood(weather: String, mood: String) {
        // Update the UI state with new weather and mood
        _uiState.value = _uiState.value.copy(weather = weather, mood = mood)
        // Reload songs based on the updated state
        loadSongsForCurrentState()
    }

    fun updateMood(newMood: String) {
        _uiState.value = _uiState.value.copy(mood = newMood)
        updateWeatherAndMood(_uiState.value.weather, newMood) // Fetch songs dynamically (if needed)
    }

    private fun loadSongsForCurrentState() {
        val currentWeather = _uiState.value.weather.split(",")[0] // Extract weather condition
        val currentMood = _uiState.value.mood
        val songs = getSongsForMoodAndWeather(currentWeather, currentMood)
        _uiState.value = _uiState.value.copy(songs = songs)
    }

    private fun getSongsForMoodAndWeather(weather: String, mood: String): List<Song> {
        val data = mapOf(
            "Rainy" to mapOf(
                "Relaxed" to listOf(
                    Song(
                        title = "Someone Like You",
                        artist = "Adele",
                        spotifyLink = "https://open.spotify.com/track/4kflIGfjdZJW4ot2ioixTB",
                        albumArt = "https://i.scdn.co/image/ab67616d00001e024df21ab598840ed9c65b0d2e"
                    ),
                    Song(
                        title = "Let Her Go",
                        artist = "Passenger",
                        spotifyLink = "https://open.spotify.com/track/0JmiBCpSbrLXtA0g2hOKqu",
                        albumArt = "https://i.scdn.co/image/ab67616d00001e0299e8d73f843301c510d5b8e3"
                    )
                ),
                "Energetic" to listOf(
                    Song(
                        title = "Thunderstruck",
                        artist = "AC/DC",
                        spotifyLink = "https://open.spotify.com/track/57bgtoPSgt236HzfBOd8kj",
                        albumArt = "https://i.scdn.co/image/ab67616d00001e023c2b8b3e6dd22c3bc8c606c6"
                    ),
                    Song(
                        title = "Wake Me Up",
                        artist = "Avicii",
                        spotifyLink = "https://open.spotify.com/track/1jYiIOC5d6soxkJP81fxq2",
                        albumArt = "https://i.scdn.co/image/ab67616d00001e02030a4d7d07fc0be00c3c6787"
                    )
                )
            ),
            "Sunny" to mapOf(
                "Happy" to listOf(
                    Song(
                        title = "Walking on Sunshine",
                        artist = "Katrina and the Waves",
                        spotifyLink = "https://open.spotify.com/track/4XjE6U8s3hOF4h1dGIcnnp",
                        albumArt = "https://i.scdn.co/image/ab67616d00001e022d17373959d9bb3c328ac06e"
                    ),
                    Song(
                        title = "Uptown Funk",
                        artist = "Mark Ronson ft. Bruno Mars",
                        spotifyLink = "https://open.spotify.com/track/32OlwWuMpZ6b0aN2RZOeMS",
                        albumArt = "https://i.scdn.co/image/ab67616d00001e02f1c0b1d3b6b519ac118e1b7b"
                    )
                ),
                "Relaxed" to listOf(
                    Song(
                        title = "Here Comes the Sun",
                        artist = "The Beatles",
                        spotifyLink = "https://open.spotify.com/track/6dGnYIeXmHdcikdzNNDMm2",
                        albumArt = "https://i.scdn.co/image/ab67616d00001e02442b5342c93a7317390ae4bc"
                    ),
                    Song(
                        title = "Better Together",
                        artist = "Jack Johnson",
                        spotifyLink = "https://open.spotify.com/track/3EBE8liK0KlkgslbRKslpQ",
                        albumArt = "https://i.scdn.co/image/ab67616d00001e024e507dc09d28ff7ff0288e4d"
                    )
                )
            )
        )
        return data[weather]?.get(mood) ?: emptyList()
    }

    fun openSongOnSpotify(link: String) {
        // Use Android's Intent system to open Spotify
        // This is a placeholder; implementation depends on your app's navigation logic
    }
}

data class MusicUiState(
    val weather: String,
    val mood: String,
    val songs: List<Song>
)

data class Song(
    val title: String,
    val artist: String,
    val spotifyLink: String,
    val albumArt: String
)
