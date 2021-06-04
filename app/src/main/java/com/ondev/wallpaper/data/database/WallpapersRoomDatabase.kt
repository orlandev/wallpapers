package com.ondev.wallpaper.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ondev.wallpaper.utils.DATABASE_NAME


@Database(entities = [Wallpaper::class], version = 1, exportSchema = false)
abstract class WallpapersRoomDatabase : RoomDatabase() {

    abstract fun wallpapersDao(): WallpapersDao

    companion object {

        @Volatile
        private var INSTANCE: WallpapersRoomDatabase? = null
        fun getDataBase(context: Context): WallpapersRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WallpapersRoomDatabase::class.java, DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}