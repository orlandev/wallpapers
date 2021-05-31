package com.ondev.wallpaperpro.api

import com.ondev.wallpaperpro.data.PixabayJson
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET("api/")
    suspend fun searchWallpapers(
        @Query("key") apiKey: String,
        @Query("q", encoded = true) userSearch: String,
        @Query("image_type") imageType: String = "photo",
        @Query("pretty") pretty: Boolean = true,
        @Query("orientation") orientation: String = "horizontal",
        @Query("per_page") per_page: Int = 100,
        @Query("safesearch") safesearch: Boolean = true,
    ): PixabayJson
}