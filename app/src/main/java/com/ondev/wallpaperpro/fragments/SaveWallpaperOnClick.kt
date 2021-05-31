package com.ondev.wallpaperpro.fragments

import com.ondev.wallpaperpro.data.database.Wallpaper

interface SaveWallpaperOnClick {
    fun saveOnClick(newWallpaper: Wallpaper)
}