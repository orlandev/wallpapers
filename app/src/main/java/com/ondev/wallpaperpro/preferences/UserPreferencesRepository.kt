package com.ondev.wallpaperpro.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val USER_PAY = booleanPreferencesKey("user_pay")
        val FIRST_INDEX = booleanPreferencesKey("first_index")
    }

    suspend fun getPrefs(): UserPreferences {
        return dataStore.data.map { preferences ->
            val userPay = preferences[PreferencesKeys.USER_PAY] ?: false
            UserPreferences(
                userPay
            )
        }.first()
    }

    suspend fun isFirstIndex(): Boolean {
        return dataStore.data.map { preferences ->
            val isFirstIndex = preferences[PreferencesKeys.FIRST_INDEX] ?: false
            isFirstIndex
        }.first()
    }

    suspend fun updateUserPay(isUserPay: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_PAY] = isUserPay
        }
    }

    suspend fun setFirstIndex(isIndexed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_INDEX] = isIndexed
        }
    }
}