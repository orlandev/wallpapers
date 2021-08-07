package com.ondev.wallpaper.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers")
data class Wallpaper(
    var url: String,
    var owner: String,
    var ownerAvatarUrl: String
){
    @PrimaryKey(autoGenerate = true)  var id: Int = 0
}
