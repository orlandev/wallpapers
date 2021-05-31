package com.ondev.wallpaperpro.adapters

import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.ondev.wallpaperpro.imageloader.ImageLoader
import com.ondev.wallpaperpro.R
import com.ondev.wallpaperpro.data.Hit
import com.ondev.wallpaperpro.data.database.Wallpaper
import com.ondev.wallpaperpro.databinding.ImagesListItemBinding
import com.ondev.wallpaperpro.fragments.SaveWallpaperOnClick

class SearchListViewHolder(
    private val binding: ImagesListItemBinding,
    private val saveWallpaperOnClick: SaveWallpaperOnClick,
    private val imageLoader: ImageLoader
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(pixabayHits: Hit) {
        Log.d("AQUIIIII", "bindData: ${pixabayHits.user}")

        imageLoader.loadImage(
            Uri.parse(pixabayHits.webformatURL).toString(),
            binding.imageItem,
            R.drawable.download,
            R.drawable.download
        )
        imageLoader.loadImage(
            Uri.parse(pixabayHits.userImageURL).toString(),
            binding.userAvatar,
            R.drawable.download,
            R.drawable.download
        )

        binding.userName.text = pixabayHits.user
        binding.textImageDownloads.text = pixabayHits.downloads.toString()
        binding.textImageLikes.text = pixabayHits.likes.toString()

        binding.downloadButton.setOnClickListener {
            saveWallpaperOnClick.saveOnClick(
                Wallpaper(
                    0,
                    pixabayHits.webformatURL,
                    pixabayHits.user,
                    pixabayHits.userImageURL
                )
            )
        }
    }

}
