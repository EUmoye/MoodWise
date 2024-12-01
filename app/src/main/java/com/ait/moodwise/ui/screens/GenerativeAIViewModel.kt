package com.ait.moodwise.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.ait.moodwise.data.activity.Activities
import com.ait.moodwise.data.activity.ActivitiesItem
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.serialization.json.Json

class GenAIViewModel: ViewModel() {
    //    private val generativeModel = GenerativeModel(
//        modelName = "gemini-pro",
//        apiKey = "YOUR_API_KEY" // AIzaSyDVNZGkZjPOEqk76-1SfhJmYzxmiE0l4es
//    )
//
//    private val _textGenerationResult = MutableStateFlow<String?>(null)
//    val textGenerationResult = _textGenerationResult.asStateFlow()
//
//    fun generateJoke(prompt: String = "Tell me a joke") {
//        _textGenerationResult.value = "Generating the joke..."
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val result = generativeModel.generateContent(prompt)
//                _textGenerationResult.value = result.text
//            } catch (e: Exception) {
//                _textGenerationResult.value = "Error: ${e.message}"
//            }
//        }
//    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun json_no_schema(): List<ActivitiesItem>{
        val generativeModel =
            GenerativeModel(
                // Specify a Gemini model appropriate for your use case
                modelName = "gemini-1.5-flash",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            )
        var prompt = """
        Generate 6 activities for me to do in 10 degrees celius weather in Budapest using this JSON schema:
        [
            { "name": "String", "location": "String", "description": "String" }
        ]
    """.trimIndent()

        val response = generativeModel.generateContent(prompt)
//        print(response.text)
        val jsonString = response.text?.replace("```json", "")?.replace("```", "")?.trim()
//        Log.d("the type of json", jsonString!!::class.java.typeName)
//        Log.d()
//        print(jsonString)

        val activities = jsonString?.let { parseActivities(it) }
        return activities!!
////        return response
//        Log.d("response", activities.toString())
//        Log.d("parsed activities", activities.toString())
//        print(activities)
//        print(activities)
    }


    fun parseActivities(jsonString: String): List<ActivitiesItem> {
//        val activities = Activities()
//        jsonString.forEach { string ->
//            val activityItem: ActivitiesItem = Json.decodeFromString<ActivitiesItem>(string)
//            activities.add(activityItem)
//        }
        return Json.decodeFromString<List<ActivitiesItem>>(jsonString)
//        return activities
    }
}