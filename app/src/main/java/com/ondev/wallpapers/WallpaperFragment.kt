package com.ondev.wallpapers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ondev.wallpapers.databinding.FragmentWallpaperViewpagerBinding


class WallpaperFragment : Fragment() {
    private lateinit var binding: FragmentWallpaperViewpagerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Inflate the layout for this fragment

        FragmentWallpaperViewpagerBinding.inflate(inflater, container, false).also { binding = it }

        val wallpaperItems =
            (1..25).map { WallpaperItem("${ASSETS_FOLDER}images/wallpaper$it.jpg") }

        binding.viewPageWallpaper.adapter = WallpaperAdapter(
            wallpaperItems
        )

        binding.viewPageWallpaper.setPageTransformer(WallpaperTransformer())

        return binding.root
    }


}
