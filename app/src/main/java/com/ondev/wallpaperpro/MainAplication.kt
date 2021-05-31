package com.ondev.wallpaperpro

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.inmersoft.trinidadpatrimonial.core.imageloader.GlideImageLoader
import com.ondev.wallpaperpro.imageloader.ImageLoader
import com.ondev.wallpaperpro.data.database.WallpapersRepository
import com.ondev.wallpaperpro.data.database.WallpapersRoomDatabase
import com.ondev.wallpaperpro.preferences.UserPreferencesRepository

class MainAplication : Application() {

    private val USER_PREFERENCES_NAME = "user_preferences"

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    private val database by lazy { WallpapersRoomDatabase.getDataBase(this) }
    val wallpapersRepository by lazy { WallpapersRepository(database.wallpapersDao()) }
    val userPrefsRepo by lazy { UserPreferencesRepository(dataStore) }
    val imageLoader: ImageLoader by lazy { GlideImageLoader(this) }
}