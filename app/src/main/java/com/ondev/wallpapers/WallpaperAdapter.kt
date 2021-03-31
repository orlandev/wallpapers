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
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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
                setWallpaper(it, currentWallpaperItem.wallpaperFileName)
            }
        })

        Glide.with(holder.imageWallpaper.context)
            .load(Uri.parse(currentWallpaperItem.wallpaperFileName))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageWallpaper)
    }

    private fun setWallpaper(view: View, filename: String) {
        Glide.with(view.context)
            .asBitmap().load(filename)
            .listener(object : RequestListener<Bitmap?> {
                override fun onLoadFailed(
                    @Nullable e: GlideException?,
                    o: Any?,
                    target: Target<Bitmap?>?,
                    b: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    bitmap: Bitmap?,
                    o: Any?,
                    target: Target<Bitmap?>?,
                    dataSource: DataSource?,
                    b: Boolean
                ): Boolean {
                    //zoomImage.setImage(ImageSource.bitmap(bitmap))
                    val wallpaperManager =
                        WallpaperManager.getInstance(view.context.applicationContext)
                    wallpaperManager.setBitmap(bitmap)
                    Log.d("SET_WALLPAPER", "WALLPAPER SETTED")
                    return false
                }
            }
            ).submit()

    }


    override fun getItemCount(): Int {
        return wallpaperItems.size
    }
}