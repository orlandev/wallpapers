package com.ondev.wallpaper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ondev.wallpaper.data.database.Wallpaper
import com.ondev.wallpaper.databinding.FragmentWallpaperScreenBinding
import com.ondev.wallpaper.fragments.IAndroidApi
import com.ondev.wallpaper.imageloader.ImageLoader


class WallpaperAdapter(
    private val androidApiCalls: IAndroidApi, private val imageLoader: ImageLoader
) : ListAdapter<Wallpaper, WallpaperViewHolder>(WallpaperDiffUtil()) {
    class WallpaperDiffUtil : DiffUtil.ItemCallback<Wallpaper>() {
        override fun areItemsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Wallpaper, newItem: Wallpaper): Boolean {
            return oldItem.owner == newItem.owner
                    && oldItem.ownerAvatarUrl == newItem.ownerAvatarUrl
                    && oldItem.url == newItem.url
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WallpaperViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentWallpaperScreenBinding.inflate(inflater, parent, false)
        return WallpaperViewHolder(
            binding, androidApiCalls, imageLoader
        )
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val currentWallpaperItem = getItem(position)
        holder.bindData(currentWallpaperItem, itemCount, position)
    }

}