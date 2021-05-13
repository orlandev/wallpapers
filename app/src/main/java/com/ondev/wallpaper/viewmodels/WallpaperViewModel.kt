package com.ondev.wallpaper.viewmodels

import VolleySingleton
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.ondev.wallpaper.data.database.Wallpaper
import com.ondev.wallpaper.data.database.WallpapersRepository
import com.ondev.wallpaper.utils.API_PIXABAY_URL
import com.ondev.wallpaper.utils.USER_SEARCH
import org.json.JSONArray
import org.json.JSONObject

class WallpapersViewModel(private val repository: WallpapersRepository) : ViewModel() {
    val allWallpapers: LiveData<List<Wallpaper>> = repository.allWallpapers
    fun getRepository(): WallpapersRepository = repository
    suspend fun insert(wallpaper: Wallpaper) = repository.insert(wallpaper)
    suspend fun update(wallpaper: Wallpaper) = repository.update(wallpaper)
    suspend fun deleteByID(id: Int) = repository.deleteByID(id)

    fun fetchData(ctx: Context, userSearchInput: String) {
        val currentUrl = API_PIXABAY_URL.replace(USER_SEARCH, userSearchInput.replace(" ", "+"))
        var pixabayRequjest = JsonObjectRequest(Request.Method.GET, currentUrl, null,
            { response ->
                try {
                    val arrayHits: JSONArray = response.getJSONArray("hits")
                    for (i in 0..arrayHits.length()) {
                        val jsonObject: JSONObject = arrayHits.getJSONObject(i)
                        val url = jsonObject.getString("webformatURL")
                        Log.d("PIXABAY", "fetchData: URL: $url")
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                Log.d("PIXABAY", "fetchData: $response")
            },
            { error ->
                Log.e("PIXABAY", "fetchData: ${error.message}")
            }
        )
        // Access the RequestQueue through your singleton class.
        VolleySingleton.getInstance(ctx).addToRequestQueue(pixabayRequjest)
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