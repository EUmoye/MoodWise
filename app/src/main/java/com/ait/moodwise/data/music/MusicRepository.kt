package com.ait.moodwise.data.music

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
            suggest a good comprehensive list of songs to listen to that would be suitable for this mood.
            Provide the response in this JSON format:
            [
                {"name": "Song Name", "artist": "Artist Name"}
            ]
        """.trimIndent()

        try {
            val response = generativeModel.generateContent(prompt).text ?: return@withContext emptyList<Song>()
            println(response)
            val jsonArray = JSONArray(response)
            List(jsonArray.length()) { index ->
                val songJson = jsonArray.getJSONObject(index)
                Song(
                    title = songJson.getString("name"),
                    artist = songJson.getString("artist"),
                    youtubeLink = "",
                    albumArt = ""
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList<Song>()
        }
    }
}
