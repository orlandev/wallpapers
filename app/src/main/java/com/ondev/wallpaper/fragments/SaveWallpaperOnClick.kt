package com.ondev.wallpaper.fragments

import com.ondev.wallpaper.data.database.Wallpaper

interface SaveWallpaperOnClick {
    fun saveOnClick(newWallpaper: Wallpaper)
}