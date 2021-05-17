package com.ondev.wallpaper.data.database

import androidx.lifecycle.LiveData
import com.ondev.wallpaper.api.Network
import com.ondev.wallpaper.utils.API_KEY


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

    suspend fun fetchWallpapers(userSearch: String): List<Wallpaper>? {
        val data = Network.pixabayApi?.searchWallpapers(API_KEY, userSearch)
        var newWalls = mutableListOf<Wallpaper>()
        var hits = data?.hits
        if (!hits.isNullOrEmpty()) {
            for (wall in hits) {
                newWalls.add(Wallpaper(0, wall.webformatURL, wall.user))
            }
        }
        return newWalls
    }
}