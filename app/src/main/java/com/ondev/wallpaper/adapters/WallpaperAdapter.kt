package com.ondev.wallpaper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ondev.wallpaper.data.database.Wallpaper
import com.ondev.wallpaper.databinding.FragmentWallpaperScreenBinding
import com.ondev.wallpaper.fragments.IAndroidApi
import com.ondev.wallpaper.imageloader.ImageLoader


class WallpaperAdapter(
    private val androidApiCalls: IAndroidApi, private val imageLoader: ImageLoader
) : RecyclerView.Adapter<WallpaperViewHolder>() {

    private val wallpaperItems = mutableListOf<Wallpaper>()

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

    fun setData(newWallsList: List<Wallpaper>) {
        wallpaperItems.clear()
        wallpaperItems.addAll(newWallsList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val currentWallpaperItem = wallpaperItems[position]
        holder.bindData(currentWallpaperItem, wallpaperItems.size, position)
    }

    override fun getItemCount(): Int {
        return wallpaperItems.size
    }
}