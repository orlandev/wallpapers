package com.ondev.wallpaperpro.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers")
data class Wallpaper(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var url: String,
    var owner: String,
    var ownerAvatarUrl: String
)
