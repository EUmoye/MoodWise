package com.ait.moodwise.di

import com.ait.moodwise.network.WeatherAPI
import com.google.ai.client.generativeai.GenerativeModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WeatherRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SpotifyRetrofit

@Module
@InstallIn(SingletonComponent::class)
object APIModule {
    private const val SPOTIFY_BASE_URL = "https://accounts.spotify.com/"
    private const val WEATHER_BASE_URL = "https://api.openweathermap.org/"

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @WeatherRetrofit
    @Singleton
    fun provideWeatherAPIRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(
                Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()) )
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherAPI(@WeatherRetrofit retrofit: Retrofit): WeatherAPI {
        return retrofit.create(WeatherAPI::class.java)
    }

//    @SpotifyRetrofit
//    @OptIn(ExperimentalSerializationApi::class)
//    @Provides
//    @Singleton
//    fun provideSpotifyAPIRetrofit(): Retrofit {
//        val client = OkHttpClient.Builder()
//            .build()
//
//        return Retrofit.Builder()
//            .baseUrl(SPOTIFY_BASE_URL)
//            .addConverterFactory(
//                Json{ ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()) )
//            .client(client)
//            .build()
//    }


//    @Provides
//    @Singleton
//    fun provideSpotifyAPI( @SpotifyRetrofit retrofit: Retrofit): SpotifyAPI {
//        return retrofit.create(SpotifyAPI::class.java)
//    }


    @Provides
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(modelName = "gemini-pro", apiKey = "AIzaSyAGy3uMnrDivcAI2Q5OGOMubVRiFgXR5xM")
    }

}