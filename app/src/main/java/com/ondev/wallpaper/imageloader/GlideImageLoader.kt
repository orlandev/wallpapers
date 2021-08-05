package com.inmersoft.trinidadpatrimonial.core.imageloader

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ondev.wallpaper.R
import com.ondev.wallpaper.imageloader.ImageLoader
import kotlin.random.Random

class GlideImageLoader(private val context: Context) : ImageLoader {


   private val placeholderError =
        listOf<Int>(
            R.drawable.placeholder_1,
            R.drawable.placeholder_2,
            R.drawable.placeholder_3,
            R.drawable.placeholder_4,
            R.drawable.placeholder_5,
            R.drawable.placeholder_6,
            R.drawable.placeholder_7,
            R.drawable.placeholder_8,
            R.drawable.placeholder_9,
            R.drawable.placeholder_10,
            R.drawable.placeholder_11)

    override fun loadImage(imageUrl: String?, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .error(placeholderError[Random.nextInt(placeholderError.size)])
            .placeholder(placeholderError[Random.nextInt(placeholderError.size)])
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

}