package com.ondev.wallpaperpro.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PixabayJson(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int
)