package com.ondev.wallpaper.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WallpapersDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wallpaper: Wallpaper)

    @Update
    suspend fun update(wallpaper: Wallpaper)

    @Query("SELECT * FROM wallpapers")
    fun getWallpapers(): LiveData<List<Wallpaper>>

    @Query("DELETE FROM wallpapers")
    suspend fun deleteAll()

    @Query("DELETE FROM wallpapers WHERE id = :id")
    suspend fun deleteByID(id: Int)

}