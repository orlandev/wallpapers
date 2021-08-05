package com.ondev.wallpaper.imageloader

import android.widget.ImageView

interface ImageLoader {
    fun loadImage(imageUrl: String?, imageView: ImageView)
}