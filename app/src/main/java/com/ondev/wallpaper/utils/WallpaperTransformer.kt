package com.ondev.wallpaper.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.card.MaterialCardView
import com.ondev.wallpaper.R


class WallpaperTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val image = page.findViewById<ImageView>(R.id.image_view_parallax_effect)
        val setWallpaper = page.findViewById<ImageView>(R.id.set_wallpaper)
        val shareApp = page.findViewById<ImageView>(R.id.share_app)
        val textPhotoCount = page.findViewById<TextView>(R.id.photo_count)
        val aboutBtn = page.findViewById<ImageView>(R.id.about_app)
        val materialCardView = page.findViewById<MaterialCardView>(R.id.materialCardView)
        val backWallpaper = page.findViewById<ImageView>(R.id.back_wallpaper)
        val nextWallpaper = page.findViewById<ImageView>(R.id.next_wallpaper)

        image.translationX = -position * (pageWidth / 2)
        setWallpaper.translationX = position * (pageWidth / 2)
        shareApp.translationX = position * (pageWidth / 2)
        nextWallpaper.translationX = position * (pageWidth / 2)
        backWallpaper.translationX = position * (pageWidth / 2)
        aboutBtn.translationX = position * (pageWidth / 2)
        textPhotoCount.translationX = position * (pageWidth / 2)
        materialCardView.translationX = position * (pageWidth / 2)

        // SplashDescriptiom.translationX = position * (pageWidth / 2)
    }
}