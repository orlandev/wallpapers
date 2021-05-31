package com.ondev.wallpaperpro.adapters

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.net.Uri
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.ondev.wallpaperpro.R
import com.ondev.wallpaperpro.data.database.Wallpaper
import com.ondev.wallpaperpro.databinding.FragmentWallpaperScreenBinding
import com.ondev.wallpaperpro.fragments.IAndroidApi
import com.ondev.wallpaperpro.imageloader.ImageLoader
import com.ondev.wallpaperpro.utils.shimmerSetup

class WallpaperViewHolder(
    private val binding: FragmentWallpaperScreenBinding,
    private val androidApiCalls: IAndroidApi, private val imageLoader: ImageLoader
) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("InlinedApi")
    fun bindData(currentWallpaperItem: Wallpaper, maxWallpapers: Int, position: Int) {

        binding.shimmerViewContainer.setShimmer(shimmerSetup().build())

        if (currentWallpaperItem.owner.isNotEmpty()) {
            binding.photoOwner.visibility = View.VISIBLE
            binding.ownerAvatarContainer.visibility = View.VISIBLE
            binding.photoOwner.text = "${currentWallpaperItem.owner}"
            imageLoader.loadImage(
                currentWallpaperItem.ownerAvatarUrl,
                binding.ownerAvatar,
                R.drawable.download,
                R.drawable.download
            )
        } else {
            binding.photoOwner.visibility = View.INVISIBLE
            binding.ownerAvatarContainer.visibility = View.INVISIBLE
        }


        binding.download.setOnClickListener {
            androidApiCalls.navToPixabayFragment()
        }

        binding.photoCount.text = "${position + 1} / $maxWallpapers"

        binding.aboutApp.setOnClickListener {
            androidApiCalls.navToAboutAppFragment()
        }

        imageLoader.loadImage(
            Uri.parse(currentWallpaperItem.url).toString(),
            binding.imageViewParallaxEffect,
            R.drawable.download,
            R.drawable.download
        )

        binding.backWallpaper.setOnClickListener {
            androidApiCalls.backWallpaperPage()
        }

        binding.nextWallpaper.setOnClickListener {
            androidApiCalls.nextWallpaperPage()
        }

        binding.setWallpaper.setOnClickListener {
            androidApiCalls.showAlertAndSetWallpaper(
                binding.imageViewParallaxEffect.drawable.toBitmap(),
                WallpaperManager.FLAG_SYSTEM
            )
        }

        binding.setLockScreen.setOnClickListener { view ->
            androidApiCalls.showAlertAndSetWallpaper(
                binding.imageViewParallaxEffect.drawable.toBitmap(),
                WallpaperManager.FLAG_LOCK
            )
        }

        binding.shareApp.setOnClickListener(View.OnClickListener {
            androidApiCalls.sharedApp()
        })

    }

}
