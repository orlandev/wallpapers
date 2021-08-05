package com.ondev.wallpaper.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ondev.wallpaper.imageloader.ImageLoader
import com.ondev.wallpaper.data.Hit
import com.ondev.wallpaper.databinding.ImagesListItemBinding
import com.ondev.wallpaper.fragments.SaveWallpaperOnClick


class SearchListAdapter(
    private val saveWallpaperOnClick: SaveWallpaperOnClick, private val imageLoader: ImageLoader
) : ListAdapter<Hit, SearchListViewHolder>(SearchDiffUtil()) {

    class SearchDiffUtil : DiffUtil.ItemCallback<Hit>() {
        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
                    && oldItem.user_id == newItem.user_id
                    && oldItem.user == newItem.user
                    && oldItem.largeImageURL == newItem.largeImageURL
        }

        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }


    }


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


    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        val currentPixabayHit = getItem(position)
        holder.bindData(currentPixabayHit)
    }

}

