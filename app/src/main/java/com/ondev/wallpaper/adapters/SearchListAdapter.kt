package com.ondev.wallpaper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ondev.wallpaper.imageloader.ImageLoader
import com.ondev.wallpaper.data.Hit
import com.ondev.wallpaper.databinding.ImagesListItemBinding
import com.ondev.wallpaper.fragments.SaveWallpaperOnClick


class SearchListAdapter(
    private val saveWallpaperOnClick: SaveWallpaperOnClick, private val imageLoader: ImageLoader
) : RecyclerView.Adapter<SearchListViewHolder>() {

    private val jsonPixabayHits = mutableListOf<Hit>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImagesListItemBinding.inflate(inflater, parent, false)
        return SearchListViewHolder(
            binding, saveWallpaperOnClick, imageLoader = imageLoader
        )
    }

    fun setData(newData: List<Hit>) {
        jsonPixabayHits.clear()
        jsonPixabayHits.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        val currentPixabayHit = jsonPixabayHits[position]
        holder.bindData(currentPixabayHit)
    }

    override fun getItemCount(): Int {
        return jsonPixabayHits.size
    }
}

