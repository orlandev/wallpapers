package com.ondev.wallpaper

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.ondev.wallpaper.preferences.UserPreferencesRepository

class MainAplication : Application() {

    private val USER_PREFERENCES_NAME = "user_preferences"

    private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
    )

    val userPrefsRepo by lazy { UserPreferencesRepository(dataStore) }

}