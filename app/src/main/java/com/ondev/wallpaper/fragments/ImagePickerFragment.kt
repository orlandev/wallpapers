package com.ondev.wallpaper.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ondev.wallpaper.MainAplication
import com.ondev.wallpaper.databinding.ImagePickerFragmentBinding
import com.ondev.wallpaper.viewmodels.WallpaperViewModelFactory
import com.ondev.wallpaper.viewmodels.WallpapersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ImagePickerFragment : Fragment() {

    lateinit var binding: ImagePickerFragmentBinding

    private val wallpaperViewModel: WallpapersViewModel by viewModels {
        WallpaperViewModelFactory((requireContext().applicationContext as MainAplication).wallpapersRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            ImagePickerFragmentBinding.inflate(inflater, container, false)

        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        binding.recycleImagesList.layoutManager = staggeredGridLayoutManager

        searchAndShowWallpapers("")

        binding.searchButton.setOnClickListener {
            val userSearchText = binding.userSearch.editText!!.text.toString()
            if (userSearchText.isNotEmpty()) {
searchAndShowWallpapers(userSearchText = userSearchText)
            } else {
                searchAndShowWallpapers("")
            }
        }

        return binding.root
    }

    fun searchAndShowWallpapers(userSearchText:String)
    {
        lifecycleScope.launch(Dispatchers.IO) {
            val listWalls = wallpaperViewModel.searchWallpaper(userSearch = userSearchText)
        }
    }

}