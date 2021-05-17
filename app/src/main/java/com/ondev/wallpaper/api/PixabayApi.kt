package com.ondev.wallpaper.api

import com.ondev.wallpaper.data.PixabayJson
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("api/")
    suspend fun searchWallpapers(
        @Query("key") apiKey: String,
        @Query("q", encoded = true) userSearch: String,
        @Query("image_type") imageType: String = "photo"
    ): PixabayJson


}