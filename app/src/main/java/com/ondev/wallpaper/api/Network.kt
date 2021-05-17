package com.ondev.wallpaper.api

import com.ondev.wallpaper.BuildConfig
import com.ondev.wallpaper.utils.API_PIXABAY_URL
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object Network {
    var pixabayApi: PixabayApi? = null
    init {
        val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .build()

        val moshi = Moshi.Builder().build()

        pixabayApi = Retrofit.Builder()
            .baseUrl(API_PIXABAY_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PixabayApi::class.java)
    }
}