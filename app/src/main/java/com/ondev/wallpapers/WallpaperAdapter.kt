package com.ondev.wallpapers

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WallpaperAdapter(

    private var wallpaperItems: List<WallpaperItem>


) : RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {
    inner class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageWallpaper: ImageView = itemView.findViewById(R.id.image_view_parallax_effect)
        val backWallpaper: ImageView = itemView.findViewById(R.id.back_wallpaper)
        val setWallpaper: ImageView = itemView.findViewById(R.id.set_wallpaper)
        val setWallProgress: ProgressBar = itemView.findViewById(R.id.progress_bar)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WallpaperViewHolder {
        return WallpaperViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_wallpaper_screen, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val currentWallpaperItem = wallpaperItems[position]

        holder.backWallpaper.setOnClickListener(View.OnClickListener {
            Log.d("BACK_WALLPAPER", "Clicked!!")
        })

        holder.setWallpaper.setOnClickListener(View.OnClickListener {
            GlobalScope.launch {
                setWallpaper(it, holder.imageWallpaper.drawable.toBitmap())
            }
        })


        Glide.with(holder.imageWallpaper.context)
            .load(Uri.parse(currentWallpaperItem.wallpaperFileName))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageWallpaper)
    }

    private fun setWallpaper(view: View, bitmap: Bitmap) {

        val wallpaperManager =
            WallpaperManager.getInstance(view.context.applicationContext)
        wallpaperManager.setBitmap(bitmap)
        Log.d("SET_WALLPAPER", "WALLPAPER SETTED")

    }


    override fun getItemCount(): Int {
        return wallpaperItems.size
    }
}