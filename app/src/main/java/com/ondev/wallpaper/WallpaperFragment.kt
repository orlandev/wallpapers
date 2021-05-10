package com.ondev.wallpaper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ondev.wallpaper.databinding.FragmentWallpaperViewpagerBinding
import com.ondev.wallpaper.preferences.UserPreferencesRepository
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


class WallpaperFragment : Fragment() {
    private lateinit var binding: FragmentWallpaperViewpagerBinding
    private lateinit var userPref: UserPreferencesRepository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        userPref = ((requireContext().applicationContext as MainAplication).userPrefsRepo)

        UnsplashPhotoPicker.init(
            this.requireActivity().application, // application
            "awlPMWQ8oTGimCN91aL-M6zyH1EqDH6IHUhMy6Qi0wY",
            "rpVeEdKML23HUaqlIbr4dnlSoZhFnG6aE6iXVTb2iZU"
            /* optional page size */
        )

        FragmentWallpaperViewpagerBinding.inflate(inflater, container, false).also { binding = it }
        var wallpaperItems = mutableListOf<WallpaperItem>()

        try {
            val wallpaperFolder = resources.assets.list("wallpapers")
            wallpaperFolder!!.indices
                .asSequence()
                .map { wallpaperFolder[it] }
                .mapTo(wallpaperItems) { WallpaperItem("${ASSETS_FOLDER}wallpapers/$it") }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        binding.viewPageWallpaper.adapter = WallpaperAdapter(
            wallpaperItems
        )
        binding.viewPageWallpaper.setPageTransformer(WallpaperTransformer())
        binding.download.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                if (userPref.getPrefs().userPay) {
                    startUnsplash()
                } else {
                    Log.d("TAG", "onCreateView: USER NOT PAYED then pay")
                    userPref.updateUserPay(true)
                }
            }


        }
        return binding.root

    }

    fun startUnsplash() {
        startActivityForResult(
            UnsplashPickerActivity.getStartingIntent(
                requireContext(), // context
                isMultipleSelection = true
            ), UNSPLASH_REQUEST_CODE
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == UNSPLASH_REQUEST_CODE) {
            val photos: ArrayList<UnsplashPhoto>? =
                data?.getParcelableArrayListExtra(UnsplashPickerActivity.EXTRA_PHOTOS)
            var newPhotos = mutableListOf<WallpaperItem>()
            for (photo in photos!!) {
                newPhotos.add(
                    WallpaperItem(
                        photo.urls.small,
                        "Fotógrafo ${photo.user.name} en Unsplash.com"
                    )
                )
            }
            binding.viewPageWallpaper.adapter = WallpaperAdapter(newPhotos)
        } else {
            Log.d("PHOTOS", "ERROR")
        }
    }
}



