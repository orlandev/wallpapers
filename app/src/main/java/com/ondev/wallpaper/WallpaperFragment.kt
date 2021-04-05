package com.ondev.wallpaper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ondev.wallpaper.databinding.FragmentWallpaperViewpagerBinding
import java.io.IOException


class WallpaperFragment : Fragment() {
    private lateinit var binding: FragmentWallpaperViewpagerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        FragmentWallpaperViewpagerBinding.inflate(inflater, container, false).also { binding = it }

        binding.aboutApp.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_wallpaperFragment_to_about)
        })

        var wallpaperItems = mutableListOf<WallpaperItem>()

        try {
            val wallpaperFolder = resources.assets.list("wallpapers")
            wallpaperFolder!!.indices
                .asSequence()
                .map { wallpaperFolder[it] }
                .mapTo(wallpaperItems) { WallpaperItem("${ASSETS_FOLDER}wallpapers/$it") }
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
