package com.ait.moodwise.ui.screens.music
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ait.moodwise.data.music.MusicRepository
import com.ait.moodwise.data.music.YouTubeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MusicViewModel(
    private val musicRepository: MusicRepository = MusicRepository(),
    private val youtubeRepository: YouTubeRepository = YouTubeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState = _uiState.asStateFlow()

    init {
        generateNewSongs()
    }

    fun updateSongList(newSongs: List<Song>) {
        _uiState.value = _uiState.value.copy(songs = newSongs)
    }

    fun updateMood(newMood: String) {
        val currentWeather = _uiState.value.weather
        updateWeatherAndMood(currentWeather, newMood)
    }

    fun updateWeatherAndMood(weather: String, mood: String) {
        _uiState.value = _uiState.value.copy(weather = weather, mood = mood, isLoading = true)
        fetchSongs(weather, mood)
    }

    fun generateNewSongs() {
        val currentMood = _uiState.value.mood
        val currentWeather = _uiState.value.weather
        updateWeatherAndMood(currentWeather, currentMood)
    }

    fun openSongOnYouTube(context: Context, youtubeUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    suspend fun fetchSongsFromAPI() {
        val fetchedSongs = musicRepository.fetchSongs("Clear", "Sad")
        _uiState.value = _uiState.value.copy(songs = fetchedSongs)
    }

    private fun fetchSongs(weather: String, mood: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val geminiSongs = musicRepository.fetchSongs(weather, mood)
                val updatedSongs = geminiSongs.mapNotNull { geminiSong ->
                    val youtubeLink = youtubeRepository.searchYouTubeVideo(geminiSong.title, geminiSong.artist)
                    Log.d("MusicViewModel", "Youtube Link: $youtubeLink")
                    val thumbnail = youtubeLink?.let {
                        val videoId = it.substringAfter("v=")
                        "https://img.youtube.com/vi/$videoId/0.jpg"
                    }
                    if (youtubeLink != null) {
                        geminiSong.copy(
                            youtubeLink = youtubeLink,
                            albumArt = thumbnail ?: ""
                        )
                    } else null
                }

                _uiState.value = _uiState.value.copy(
                    songs = updatedSongs,
                    isLoading = false
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    songs = emptyList(),
                    isLoading = false
                )
            }
        }
    }
}

data class MusicUiState(
    val mood: String = "Sad",
    val weather: String = "Clear",
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = false
)

data class Song(
    val title: String,
    val artist: String,
    val youtubeLink: String,
    val albumArt: String
)
