package com.ait.moodwise.data.music

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class YouTubeRepository {

    private val apiKey = "AIzaSyA4Gv0Th6AP45VrYTck5EjOvwauFub2kjw"

    suspend fun searchYouTubeVideo(songName: String, artist: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val query = "${songName.replace(" ", "+")}+${artist.replace(" ", "+")}"
                val url = URL("https://www.googleapis.com/youtube/v3/search?part=snippet&q=$query&key=$apiKey&type=video")
                val connection = url.openConnection() as HttpURLConnection

                connection.requestMethod = "GET"
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    val json = JSONObject(response)
                    val items = json.getJSONArray("items")
                    if (items.length() > 0) {
                        val firstItem = items.getJSONObject(0)
                        val videoId = firstItem.getJSONObject("id").getString("videoId")
                        return@withContext "https://www.youtube.com/watch?v=$videoId"
                    }
                }
                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
