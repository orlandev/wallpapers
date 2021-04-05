package com.ondev.wallpaper

import android.view.View
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2


class WallpaperTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val image = page.findViewById<ImageView>(R.id.image_view_parallax_effect)
        val setWallpaper = page.findViewById<ImageView>(R.id.set_wallpaper)
        val shareApp = page.findViewById<ImageView>(R.id.share_app)

        val backWallpaper = page.findViewById<ImageView>(R.id.back_wallpaper)
        val nextWallpaper = page.findViewById<ImageView>(R.id.next_wallpaper)

        image.translationX = -position * (pageWidth / 2)
        setWallpaper.translationX = position * (pageWidth / 2)
        shareApp.translationX = position * (pageWidth / 2)
        nextWallpaper.translationX = position * (pageWidth / 2)
        backWallpaper.translationX = position * (pageWidth / 2)

        // SplashDescriptiom.translationX = position * (pageWidth / 2)
    }
}