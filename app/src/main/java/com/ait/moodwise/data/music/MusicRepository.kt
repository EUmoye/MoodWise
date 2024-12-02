package com.ait.moodwise.data.music

import android.util.Log
import com.ait.moodwise.ui.screens.music.Song
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

class MusicRepository {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "AIzaSyAGy3uMnrDivcAI2Q5OGOMubVRiFgXR5xM"
    )

    suspend fun fetchSongs(weather: String, mood: String): List<Song> = withContext(Dispatchers.IO) {
        val prompt = """
            Based on the current weather being "$weather" and the mood being "$mood",
            suggest a list of songs. Make sure the link you provide is valid and working please before you suggest me the song. Provide the response in this JSON format:
            [
                {"name": "Song Name", "artist": "Artist Name", "youtube_url": "YouTube URL", "album_art": "Image URL"}
            ]
        """.trimIndent()

        try {
            val response = generativeModel.generateContent(prompt).text ?: return@withContext emptyList<Song>()
            Log.d("MusicRepository", "Response: $response")

            val jsonArray = JSONArray(response)
            List(jsonArray.length()) { index ->
                val songJson = jsonArray.getJSONObject(index)
                Song(
                    title = songJson.getString("name"),
                    artist = songJson.getString("artist"),
                    youtubeLink = songJson.getString("youtube_url"),
                    albumArt = songJson.getString("album_art")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList<Song>()
        }
    }
}
