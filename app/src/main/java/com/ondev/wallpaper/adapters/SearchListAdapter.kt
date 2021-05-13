package com.ondev.wallpaper.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ondev.wallpaper.R
import com.ondev.wallpaper.data.database.Wallpaper


class SearchListAdapter(
    private var wallpaperItems: List<Wallpaper>
) : RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageWallpaper: ImageView = itemView.findViewById<ImageView>(R.id.image_item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.images_list_item, parent, false)
        return ViewHolder(
            view
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWallpaperItem = wallpaperItems[position]

        Glide.with(holder.imageWallpaper.context)
            .load(Uri.parse(currentWallpaperItem.Url))
            .placeholder(R.drawable.download)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageWallpaper)
    }

    override fun getItemCount(): Int {
        return wallpaperItems.size
    }
}