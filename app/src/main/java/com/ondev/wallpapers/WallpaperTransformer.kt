package com.ondev.wallpapers

import android.view.View
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2


class WallpaperTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val image = page.findViewById<ImageView>(R.id.image_view_parallax_effect)
        val setWallpaper = page.findViewById<ImageView>(R.id.set_wallpaper)
        val shareApp = page.findViewById<ImageView>(R.id.share_app)


        image.translationX = -position * (pageWidth / 2)
        setWallpaper.translationX = position * (pageWidth / 2)
        shareApp.translationX = position * (pageWidth / 2)
        // SplashDescriptiom.translationX = position * (pageWidth / 2)
    }
}