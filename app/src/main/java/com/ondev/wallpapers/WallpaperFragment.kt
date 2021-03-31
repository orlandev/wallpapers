package com.ondev.wallpapers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ondev.wallpapers.databinding.FragmentWallpaperViewpagerBinding
import java.io.IOException


class WallpaperFragment : Fragment() {
    private lateinit var binding: FragmentWallpaperViewpagerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        FragmentWallpaperViewpagerBinding.inflate(inflater, container, false).also { binding = it }

        var wallpaperItems = mutableListOf<WallpaperItem>()

        try {
            val wallpaperFolder = resources.assets.list("wallpapers")
            wallpaperFolder!!.indices
                .asSequence()
                .map { wallpaperFolder[it] }
                .mapTo(wallpaperItems) { WallpaperItem("${ASSETS_FOLDER}images/$it") }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        binding.viewPageWallpaper.adapter = WallpaperAdapter(
            wallpaperItems
        )
        binding.viewPageWallpaper.setPageTransformer(WallpaperTransformer())

        return binding.root
    }


}
