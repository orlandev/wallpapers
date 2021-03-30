package com.ondev.wallpapers

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class WallpaperAdapter(

    private var wallpaperItems: List<WallpaperItem>

) : RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {
    inner class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageWallpaper: ImageView = itemView.findViewById(R.id.image_view_parallax_effect)
        // val txtSplashTitle: TextView = itemView.findViewById(R.id.text_onboarding_title)
        // val txtSplashDescription: TextView = itemView.findViewById(R.id.text_onboarding_description)

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
        Log.d("CURRENT_WALLPAPER", currentWallpaperItem.WallpaperFileName)
        var tmp:String="$ASSETS_FOLDER${currentWallpaperItem.WallpaperFileName}"
        Log.d("TTTTTTTT",tmp)
        Glide.with(holder.imageWallpaper.context)
            .load(Uri.parse(tmp))
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.glide_error)
            .into(holder.imageWallpaper)

        //holder.txtSplashTitle.setText(currentOnBoardingItem.onBoardingTitle)
        // holder.txtSplashDescription.setText(currentOnBoardingItem.onBoardingDescription)
    }

    override fun getItemCount(): Int {
        return wallpaperItems.size
    }
}