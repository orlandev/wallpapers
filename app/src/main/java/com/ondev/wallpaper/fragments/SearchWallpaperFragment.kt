package com.ondev.wallpaper.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ondev.wallpaper.MainAplication
import com.ondev.wallpaper.R
import com.ondev.wallpaper.adapters.SearchListAdapter
import com.ondev.wallpaper.databinding.FragmentSearchWallpaperBinding
import com.ondev.wallpaper.viewmodels.WallpaperViewModelFactory
import com.ondev.wallpaper.viewmodels.WallpapersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class SearchWallpaperFragment : Fragment() {

    lateinit var binding: FragmentSearchWallpaperBinding
    lateinit var listAdapter: SearchListAdapter
    lateinit var recycleImagesList: RecyclerView

    val randomSearch =
        "backgrounds, fashion, nature, science, education, feelings, health, people, religion, places, animals, industry, computer, food, sports, transportation, travel, buildings, business, music"

    private val wallpaperViewModel: WallpapersViewModel by viewModels {
        WallpaperViewModelFactory((requireContext().applicationContext as MainAplication).wallpapersRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchWallpaperBinding.inflate(layoutInflater, container, false)

        recycleImagesList = binding.recycleImagesList

        binding.swipeContainer.setOnRefreshListener {
            val words = randomSearch.split(',')
            val randomNumber = Random.nextInt(0, words.size - 1)
            searchAndShowWallpapers(words[randomNumber].trim())
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_searchWallpaperFragment_to_wallpaperFragment)
        }

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

        binding.swipeContainer.isRefreshing = true

        lifecycleScope.launch(Dispatchers.IO) {
            val listWalls =
                wallpaperViewModel.searchWallpapersOnline(userSearch = userSearchText)
            Log.d("SARCHING", "searchAndShowWallpapers: Callin in COrroutine")
            lifecycleScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.Main) {
                    binding.swipeContainer.isRefreshing = false
                }
                Log.d("SARCHING", "searchAndShowWallpapers: listWallSize: ${listWalls?.size}")
                listAdapter = SearchListAdapter(requireActivity(), listWalls!!, wallpaperViewModel)
                recycleImagesList.swapAdapter(listAdapter, true)
                Log.d("SARCHING", "searchAndShowWallpapers: Calling")
            }
        }
    }

}