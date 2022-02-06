package com.example.glide

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitYouTubeData {
    private var instance: Retrofit? = null
    private const val BASE_URL = "https://www.googleapis.com"

    // SingleTon
    fun getInstance(): Retrofit {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }
}