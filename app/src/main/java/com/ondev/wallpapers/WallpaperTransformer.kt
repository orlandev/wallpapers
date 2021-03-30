package com.ondev.wallpapers

import android.view.View
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2


class WallpaperTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val pageWidth = page.width
        val image = page.findViewById<ImageView>(R.id.image_view_parallax_effect)
        //    val SplashTitle = page.findViewById<TextView>(R.id.text_onboarding_title)
        //   val SplashDescriptiom = page.findViewById<TextView>(R.id.text_onboarding_description)

        image.translationX = -position * (pageWidth / 2)
        // SplashTitle.translationX = position * (pageWidth / 2)
        // SplashDescriptiom.translationX = position * (pageWidth / 2)
    }
}