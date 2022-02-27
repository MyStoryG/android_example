package com.example.glide

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitYouTubeService {
    @GET("/youtube/v3/videos")
    fun requestVideoInformation(
        @Query("id") videoId: String,
        @Query("key") developerKey: String = "YOUR_API_KEY",
        @Query("fields") fields: String = "items(id,snippet(publishedAt,title,thumbnails),statistics(viewCount))",
        @Query("part") part: String = "snippet,statistics"
    ): Call<Video>
}