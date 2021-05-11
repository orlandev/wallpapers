package com.ondev.wallpaper.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WallpapersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(wallpaper: Wallpaper)

    @Update
    suspend fun update(wallpaper: Wallpaper)

    @Query("SELECT * FROM wallpapers")
    fun getWallpapers(): LiveData<List<Wallpaper>>

    @Query(value = "DELETE FROM wallpapers")
    suspend fun deleteAll()

    @Query(value = "DELETE FROM wallpapers where id = :id")
    suspend fun deleteByID(id: Int)

}