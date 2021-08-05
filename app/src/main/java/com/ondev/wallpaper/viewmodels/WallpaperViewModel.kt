package com.ondev.wallpaper.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ondev.wallpaper.data.Hit
import com.ondev.wallpaper.data.database.Wallpaper
import com.ondev.wallpaper.data.database.WallpapersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WallpapersViewModel(private val repository: WallpapersRepository) : ViewModel() {

    val allWallpapers: LiveData<List<Wallpaper>> = repository.allWallpapers

    fun getRepository(): WallpapersRepository = repository

    fun insert(wallpaper: Wallpaper) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(wallpaper)
        }
    }

    suspend fun update(wallpaper: Wallpaper) = repository.update(wallpaper)

    suspend fun deleteByID(id: Int) = repository.deleteByID(id)

    suspend fun searchWallpapersOnline(userSearch: String): List<Hit>? {
        return repository.searchWallpapers(userSearch)
    }
}

class WallpaperViewModelFactory(private val repository: WallpapersRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WallpapersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WallpapersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ")
    }
}