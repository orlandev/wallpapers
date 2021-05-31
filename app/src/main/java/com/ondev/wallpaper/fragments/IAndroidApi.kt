package com.ondev.wallpaper.fragments

import android.graphics.Bitmap

interface IAndroidApi {
    fun showAlertAndSetWallpaper(wallpaperAsBitmap: Bitmap, WALLPAPERMANAGER_FLAG: Int)
    fun sharedApp()
    fun nextWallpaperPage()
    fun backWallpaperPage()
    fun navToAboutAppFragment()
    fun navToPixabayFragment()
}