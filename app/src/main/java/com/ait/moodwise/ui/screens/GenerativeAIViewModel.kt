package com.ait.moodwise.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ait.moodwise.data.activity.Activities
import com.ait.moodwise.data.activity.ActivitiesItem
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class GenAIViewModel @Inject constructor() : ViewModel() {

    private val _activitiesList = MutableStateFlow<List<ActivitiesItem>>(emptyList())
    val activitiesList: StateFlow<List<ActivitiesItem>> = _activitiesList
    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchActivities(cityCountry: String) {
        viewModelScope.launch {
            try {
                val activities = json_no_schema(cityCountry)
                _activitiesList.value = activities
            } catch (e: Exception) {
                Log.d("GenAIViewModel", "Failed to fetch activities: ${e.message}")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun json_no_schema(cityCountry: String): List<ActivitiesItem>{
        var activities = { mutableStateOf<List<ActivitiesItem>>(emptyList()) }
        val generativeModel =
            GenerativeModel(
                // Specify a Gemini model appropriate for your use case
                modelName = "gemini-1.5-flash",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = "AIzaSyDVNZGkZjPOEqk76-1SfhJmYzxmiE0l4es",
            )
        var prompt = """
        Generate 6 activities for me to do in 10 degrees celius weather in ${cityCountry} using this JSON schema:
        [
            { "name": "String", "location": "String", "description": "String" }
        ]
    """.trimIndent()

        val response = generativeModel.generateContent(prompt)
        val jsonString = response.text?.replace("```json", "")?.replace("```", "")?.trim()
        val test = jsonString?.let { parseActivities(it) }
//        activities = test TODO fix this to ensure it can hold a variable to be referenced elsewhere
        return test!!
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