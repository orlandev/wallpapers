package com.ondev.wallpaper.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader
import com.ondev.wallpaper.MainAplication
import com.ondev.wallpaper.R
import com.ondev.wallpaper.adapters.SearchListAdapter
import com.ondev.wallpaper.data.database.Wallpaper
import com.ondev.wallpaper.databinding.FragmentSearchWallpaperBinding
import com.ondev.wallpaper.viewmodels.WallpaperViewModelFactory
import com.ondev.wallpaper.viewmodels.WallpapersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class SearchWallpaperFragment : Fragment(), SaveWallpaperOnClick {

    private val imageLoader: ImageLoader by lazy {
        (requireActivity().application as MainAplication).imageLoader
    }

    lateinit var binding: FragmentSearchWallpaperBinding

    lateinit var listAdapter: SearchListAdapter

    lateinit var recycleImagesList: RecyclerView


    private val randomSearch =
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

        listAdapter = SearchListAdapter(this, imageLoader)

        recycleImagesList.adapter = listAdapter

        binding.swipeContainer.setOnRefreshListener {
            val words = randomSearch.split(',')
            val randomNumber = Random.nextInt(0, words.size - 1)
            searchAndShowWallpapers(words[randomNumber].trim())
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_searchWallpaperFragment_to_wallpaperFragment)
        }


        binding.searchButton.setOnClickListener {
            val userSearchText = binding.userSearch.editText!!.text.toString()
            if (userSearchText.isNotEmpty()) {
                searchAndShowWallpapers(userSearchText = userSearchText)
            } else {
                searchAndShowWallpapers("")
            }
        }

        searchAndShowWallpapers("")

        return binding.root
    }

    private fun searchAndShowWallpapers(userSearchText: String) {
        Log.d("SARCHING", "searchAndShowWallpapers: ENTRO")

        binding.swipeContainer.isRefreshing = true

        lifecycleScope.launch(Dispatchers.IO) {
            val listWalls = wallpaperViewModel.searchWallpapersOnline(userSearch = userSearchText)
            lifecycleScope.launch(Dispatchers.Main) {
                binding.swipeContainer.isRefreshing = false
                listAdapter.setData(listWalls!!)
            }
        }
    }

    override fun saveOnClick(newWallpaper: Wallpaper) {
        lifecycleScope.launch(Dispatchers.IO) {
            wallpaperViewModel.insert(
                newWallpaper
            )
        }
        Toast.makeText(context, "Imagen guardada.", Toast.LENGTH_SHORT).show()
    }

}