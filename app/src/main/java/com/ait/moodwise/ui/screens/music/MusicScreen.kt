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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter


@Composable
fun MusicScreen(viewModel: MusicViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
//                MoodDropdown(selectedMood = state.mood, onMoodChange = { viewModel.updateWeatherAndMood(state.weather, it) })
                // Mood Selector as a LazyRow
                MoodLazyRow(
                    selectedMood = state.mood,
                    onMoodChange = { newMood -> viewModel.updateMood(newMood) }
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
                SongList(
                    songs = state.songs,
                    onSongClick = { viewModel.openSongOnSpotify(it.spotifyLink) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                NewReleasesSection()
                Spacer(modifier = Modifier.height(16.dp))
                CategorySection()
            }
        }
    )
}

@Composable
fun MoodHeader(weather: String, mood: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(16.dp)
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
}

@Composable
fun MoodDropdown(selectedMood: String, onMoodChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val moods = listOf("Happy", "Sad", "Relaxed", "Energetic", "Calm") // Add more moods if needed
    var currentMood by remember { mutableStateOf(selectedMood) }

    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        TextField(
            value = currentMood,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select your mood") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    modifier = Modifier.clickable { expanded = !expanded }
                )
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            moods.forEach { mood ->
                DropdownMenuItem(
                    text = { Text(mood) },
                    onClick = {
                        currentMood = mood
                        onMoodChange(mood) // Pass the selected mood to the parent composable
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MoodLazyRow(selectedMood: String, onMoodChange: (String) -> Unit) {
    val moods = listOf("Happy", "Sad", "Relaxed", "Energetic", "Calm", "Focus", "Chill") // Extend as needed
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(moods) { mood ->
            MoodChip(
                mood = mood,
                isSelected = mood == selectedMood,
                onClick = { onMoodChange(mood) }
            )
        }
    }
}

@Composable
fun MoodChip(mood: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) Color(0xFF7DBCD3) else Color(0xFFE0E0E0))
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
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = song.albumArt),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(song.title, style = MaterialTheme.typography.titleMedium)
                Text(song.artist, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}

@Composable
fun NewReleasesSection() {
    Text(
        text = "New Releases",
        style = MaterialTheme.typography.titleLarge,
        color = Color.DarkGray
    )
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow {
        items(5) { // Replace with dynamic data
            Card(
                modifier = Modifier
                    .size(120.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                // Replace with album cover data
                Image(
                    painter = rememberAsyncImagePainter(model = "https://via.placeholder.com/120"),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun CategorySection() {
    Text(
        text = "Categories",
        style = MaterialTheme.typography.titleLarge,
        color = Color.DarkGray
    )
    Spacer(modifier = Modifier.height(8.dp))
    LazyRow {
        val categories = listOf("Rock", "Metal", "Jazz", "Soul", "Chillout", "Classical")
        items(categories) { category ->
            Card(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { /* add the category click here */ },
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentSize()
                ) {
                    Text(text = category, color = Color.Black)
                }
            }
        }
    }
}
