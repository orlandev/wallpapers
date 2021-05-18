package com.ondev.wallpaper.adapters

import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ondev.wallpaper.R
import com.ondev.wallpaper.data.Hit
import com.ondev.wallpaper.data.database.Wallpaper
import com.ondev.wallpaper.data.database.WallpapersRoomDatabase
import com.ondev.wallpaper.viewmodels.WallpapersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchListAdapter(
    private val jsonPixabayHits: List<Hit>, private val wallpapersViewModel: WallpapersViewModel
) : RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {
    inner class SearchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageWallpaper: ImageView = itemView.findViewById<ImageView>(R.id.image_item)
        val imageUserAvatar: ImageView = itemView.findViewById<ImageView>(R.id.user_avatar)
        val userName: TextView = itemView.findViewById<TextView>(R.id.user_name)
        val imageDownload: TextView = itemView.findViewById<TextView>(R.id.text_image_downloads)
        val imageLikes: TextView = itemView.findViewById<TextView>(R.id.text_image_likes)
        val imageDownloadButton: ImageView = itemView.findViewById<ImageView>(R.id.download_button)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.images_list_item, parent, false)
        return SearchListViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        val currentWallpaperItem = jsonPixabayHits[position]

        val webFormatUrl = Uri.parse(currentWallpaperItem.webformatURL)
        val avatarUrl = Uri.parse(currentWallpaperItem.userImageURL)
        val userName = currentWallpaperItem.user
        val iDowanloads = currentWallpaperItem.downloads
        val iLikes = currentWallpaperItem.likes

        holder.imageDownload.text = iDowanloads.toString()
        holder.imageLikes.text = iLikes.toString()
        holder.userName.text = userName

        Log.d("SARCHING", "onBindViewHolder: RealURL = $webFormatUrl ")

        Glide.with(holder.itemView.context)
            .load(webFormatUrl)
            .placeholder(R.drawable.download)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageWallpaper)

        Glide.with(holder.itemView.context)
            .load(avatarUrl)
            .placeholder(R.drawable.download)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageUserAvatar)

        holder.imageDownloadButton.setOnClickListener {

            GlobalScope.launch(Dispatchers.IO) {
                wallpapersViewModel.insert(
                    Wallpaper(
                        0,
                        currentWallpaperItem.webformatURL,
                        currentWallpaperItem.user
                    )
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(it.context, "Imagen guardada.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return jsonPixabayHits.size
    }
}

