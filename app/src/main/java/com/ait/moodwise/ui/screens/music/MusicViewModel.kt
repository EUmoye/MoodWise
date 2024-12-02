package com.ait.moodwise.ui.screens.music

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ait.moodwise.data.music.MusicRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MusicUiState(
    val mood: String = "Happy",
    val weather: String = "Clear",
    val songs: List<Song> = emptyList()
)

class MusicViewModel(private val repository: MusicRepository = MusicRepository()) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState = _uiState.asStateFlow()

    fun updateMood(newMood: String) {
        val currentWeather = _uiState.value.weather // Assuming weather is already fetched
        updateWeatherAndMood(currentWeather, newMood)
    }

    fun updateWeatherAndMood(weather: String, mood: String) {
        _uiState.value = _uiState.value.copy(weather = weather, mood = mood)
        fetchSongs(weather, mood)
    }

    fun openSongOnYouTube(context: Context, youtubeUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun fetchSongs(weather: String, mood: String) {
        viewModelScope.launch {
            val songs = repository.fetchSongs(weather, mood)
            _uiState.value = _uiState.value.copy(songs = songs)
        }
    }
}

data class Song(
    val title: String,
    val artist: String,
    val youtubeLink: String,
    val albumArt: String
)