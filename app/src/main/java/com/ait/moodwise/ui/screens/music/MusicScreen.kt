package com.ait.moodwise.ui.screens.music

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun MusicScreen(viewModel: MusicViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val moods = listOf(
        "Happy", "Sad", "Relaxed", "Energetic", "Calm", "Focus", "Chill", "Angry", "Bored", "Excited"
    )

    val isLoading = state.songs.isEmpty()

    Scaffold(
        floatingActionButton = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(
                    onClick = {
                        viewModel.updateMood(moods.random())
                        viewModel.generateNewSongs()
                    },
                    containerColor = Color(0xFFEF5350),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Feeling Lucky")
                }

                FloatingActionButton(
                    onClick = { viewModel.generateNewSongs() },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Generate New Songs")
                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = getMoodGradient(state.mood)
                            )
                        )
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Under the Weather",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                        ),
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                MoodLazyRow(
                    selectedMood = state.mood,
                    onMoodChange = { newMood ->
                        viewModel.updateMood(newMood)
                        viewModel.generateNewSongs()
                    },
                    backgroundGradient = getMoodGradient(state.mood)
                )

                Spacer(modifier = Modifier.height(16.dp))

                MoodHeader(weather = state.weather, mood = state.mood)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Recommended Songs",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        color = Color(0xFF7DBCD3)
                    )
                } else {
                    SongList(
                        songs = state.songs,
                        onSongClick = { song ->
                            viewModel.openSongOnYouTube(context, song.youtubeLink)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun MoodLazyRow(
    selectedMood: String,
    onMoodChange: (String) -> Unit,
    backgroundGradient: List<Color>
) {
    val moodColors = mapOf(
        "Happy" to Color(0xFFFFC107),
        "Sad" to Color(0xFF64B5F6),
        "Relaxed" to Color(0xFFA5D6A7),
        "Energetic" to Color(0xFFFF7043),
        "Calm" to Color(0xFFBA68C8),
        "Focus" to Color(0xFF78909C),
        "Chill" to Color(0xFF81D4FA),
        "Angry" to Color(0xFFEF5350),
        "Bored" to Color(0xFFBDBDBD),
        "Excited" to Color(0xFFFFABAB)
    )
    val moods = listOf(
        "Happy", "Sad", "Relaxed", "Energetic", "Calm", "Focus", "Chill", "Angry", "Bored", "Excited"
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(moods) { mood ->
            MoodChip(
                mood = mood,
                isSelected = mood == selectedMood,
                onClick = { onMoodChange(mood) },
                moodColor = moodColors[mood] ?: Color(0xFFE59D79)
            )
        }
    }
}

@Composable
fun MoodChip(mood: String, isSelected: Boolean, onClick: () -> Unit, moodColor: Color) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) moodColor else Color(0xFFE0E0E0).copy(alpha = 0.6f)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = mood,
            color = if (isSelected) Color.White else Color.Black,
            fontSize = 14.sp
        )
    }
}


@Composable
fun SongList(songs: List<Song>, onSongClick: (Song) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(songs) { song ->
            SongCard(song = song, onClick = { onSongClick(song) })
        }
    }
}


@Composable
fun SongCard(song: Song, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(song.albumArt),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

fun getMoodGradient(mood: String): List<Color> {
    return when (mood) {
        "Happy" -> listOf(Color(0xFFFFF59D), Color(0xFFFFCC80))
        "Sad" -> listOf(Color(0xFF90CAF9), Color(0xFF42A5F5))
        "Relaxed" -> listOf(Color(0xFFA5D6A7), Color(0xFF81C784))
        "Energetic" -> listOf(Color(0xFFFFA726), Color(0xFFFF7043))
        "Calm" -> listOf(Color(0xFFCE93D8), Color(0xFFBA68C8))
        "Focus" -> listOf(Color(0xFFB0BEC5), Color(0xFF78909C))
        "Chill" -> listOf(Color(0xFFB3E5FC), Color(0xFF81D4FA))
        "Angry" -> listOf(Color(0xFFF37198), Color(0xFFEF5350))
        "Bored" -> listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD))
        "Excited" -> listOf(Color(0xFFF6A7A7), Color(0xFFFFABAB))
        else -> listOf(Color(0xFFBBDEFB), Color(0xFF90CAF9))
    }
}


@Composable
fun MoodHeader(weather: String, mood: String) {
    val gradientColors = when (mood) {
        "Happy" -> listOf(Color(0xFFFFF59D), Color(0xFFFFCC80))
        "Sad" -> listOf(Color(0xFF90CAF9), Color(0xFF42A5F5))
        "Relaxed" -> listOf(Color(0xFFA5D6A7), Color(0xFF81C784))
        "Energetic" -> listOf(Color(0xFFFFA726), Color(0xFFFF7043))
        "Calm" -> listOf(Color(0xFFCE93D8), Color(0xFFBA68C8))
        "Focus" -> listOf(Color(0xFFB0BEC5), Color(0xFF78909C))
        "Chill" -> listOf(Color(0xFFB3E5FC), Color(0xFF81D4FA))
        "Angry" -> listOf(Color(0xFFF37198), Color(0xFFEF5350))
        "Bored" -> listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD))
        "Excited" -> listOf(Color(0xFFF6A7A7), Color(0xFFFFABAB))
        else -> listOf(Color(0xFFBBDEFB), Color(0xFF90CAF9))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(brush = Brush.horizontalGradient(colors = gradientColors))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Your Daily Mood",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mood,
                style = MaterialTheme.typography.displaySmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Weather: $weather",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}

