package com.ondev.wallpaper.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ondev.wallpaper.MainAplication
import com.ondev.wallpaper.R
import com.ondev.wallpaper.adapters.SearchListAdapter
import com.ondev.wallpaper.databinding.FragmentSearchWallpaperBinding
import com.ondev.wallpaper.viewmodels.WallpaperViewModelFactory
import com.ondev.wallpaper.viewmodels.WallpapersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class SearchWallpaperFragment : Fragment() {

    lateinit var binding: FragmentSearchWallpaperBinding
    lateinit var listAdapter: SearchListAdapter
    lateinit var recycleImagesList: RecyclerView

    private val wallpaperViewModel: WallpapersViewModel by viewModels {
        WallpaperViewModelFactory((requireContext().applicationContext as MainAplication).wallpapersRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchWallpaperBinding.inflate(layoutInflater, container, false)

        /*   val staggeredGridLayoutManager =
               StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
   */
        recycleImagesList = binding.recycleImagesList

        //recycleImagesList.layoutManager = staggeredGridLayoutManager

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

    private fun searchAndShowWallpapers(userSearchText: String) {
        Log.d("SARCHING", "searchAndShowWallpapers: ENTRO")
        lifecycleScope.launch(Dispatchers.IO) {
            Log.d("SARCHING", "searchAndShowWallpapers: Callin in COrroutine")
            lifecycleScope.launch(Dispatchers.Main) {
                val listWalls =
                    wallpaperViewModel.searchWallpaper(userSearch = userSearchText)
                Log.d("SARCHING", "searchAndShowWallpapers: listWallSize: ${listWalls?.size}")
                listAdapter = SearchListAdapter(listWalls!!)
                recycleImagesList.swapAdapter(listAdapter, true)
                Log.d("SARCHING", "searchAndShowWallpapers: Calling")
            }
        }
    }

}