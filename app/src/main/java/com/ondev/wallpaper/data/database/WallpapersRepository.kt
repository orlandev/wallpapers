package com.ondev.wallpaper.data.database

import androidx.lifecycle.LiveData


// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class WallpapersRepository(private val wallpapersDao: WallpapersDao) {

    val allWallpapers: LiveData<List<Wallpaper>> = wallpapersDao.getWallpapers()

    suspend fun insert(wallpaper: Wallpaper) = wallpapersDao.insert(wallpaper)
    suspend fun update(wallpaper: Wallpaper) {
        wallpapersDao.update(wallpaper)
    }
    suspend fun deleteByID(id: Int) {
        wallpapersDao.deleteByID(id)
    }
}