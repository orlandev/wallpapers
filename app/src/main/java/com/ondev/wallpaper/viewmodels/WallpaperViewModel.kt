package com.ondev.wallpaper.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ondev.wallpaper.data.database.Wallpaper
import com.ondev.wallpaper.data.database.WallpapersRepository

class WallpapersViewModel(private val repository: WallpapersRepository) : ViewModel() {
    val allWallpapers: LiveData<List<Wallpaper>> = repository.allWallpapers
    fun getRepository(): WallpapersRepository = repository
    suspend fun insert(wallpaper: Wallpaper) = repository.insert(wallpaper)
    suspend fun update(wallpaper: Wallpaper) = repository.update(wallpaper)
    suspend fun deleteByID(id: Int) = repository.deleteByID(id)
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