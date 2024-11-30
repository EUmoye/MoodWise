package com.ait.moodwise.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ait.moodwise.data.activity.Activities
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
    suspend fun json_no_schema(){
        val generativeModel =
            GenerativeModel(
                // Specify a Gemini model appropriate for your use case
                modelName = "gemini-1.5-flash",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = "AIzaSyDVNZGkZjPOEqk76-1SfhJmYzxmiE0l4es",
            )
        var prompt = """
        Generate 6 activities for me to do in 10 degrees celius weather in Budapest using this JSON schema:
        Activity = { 'name': 'String', 'location': string, 'descriptio'n: string }
    """.trimIndent()

        val response = generativeModel.generateContent(prompt)
        print(response.text)
        val activities = response.text?.let { parseActivities(it) }
//        return response
        Log.d("parsed", activities.toString())
        print(activities)
//        print(activities)
    }


    fun parseActivities(jsonString: String): Activities {
        return Json.decodeFromString(jsonString)
    }
}