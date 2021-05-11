package com.ondev.wallpaper.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ondev.wallpaper.ASSETS_FOLDER
import com.ondev.wallpaper.MainAplication
import com.ondev.wallpaper.R
import com.ondev.wallpaper.UNSPLASH_REQUEST_CODE
import com.ondev.wallpaper.adapters.WallpaperAdapter
import com.ondev.wallpaper.data.WallpaperItem
import com.ondev.wallpaper.data.database.Wallpaper
import com.ondev.wallpaper.databinding.FragmentWallpaperViewpagerBinding
import com.ondev.wallpaper.preferences.UserPreferencesRepository
import com.ondev.wallpaper.utils.WallpaperTransformer
import com.ondev.wallpaper.viewmodels.WallpaperViewModelFactory
import com.ondev.wallpaper.viewmodels.WallpapersViewModel
import com.unsplash.pickerandroid.photopicker.UnsplashPhotoPicker
import com.unsplash.pickerandroid.photopicker.data.UnsplashPhoto
import com.unsplash.pickerandroid.photopicker.presentation.UnsplashPickerActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WallpaperFragment : Fragment() {
    private val CALL_REQUEST_PERMISSION: Int = 7854
    private lateinit var binding: FragmentWallpaperViewpagerBinding
    private lateinit var userPref: UserPreferencesRepository
    private val SMS_RECEIVE_PERMISSION_CODE = 4856
    private lateinit var USER_TRANSFER_KEY: String

    private val wallpaperViewModel: WallpapersViewModel by viewModels {
        WallpaperViewModelFactory((requireContext().applicationContext as MainAplication).wallpapersRepository)
    }

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
        lifecycleScope.launch(Dispatchers.IO) {
            if (!userPref.isFirstIndex()) {
                val wallpaperFolder = resources.assets.list("wallpapers")
                wallpaperFolder?.forEach { wallItem ->
                    wallpaperViewModel.insert(
                        Wallpaper(
                            0,
                            "${ASSETS_FOLDER}wallpapers/$wallItem",
                            ""
                        )
                    )
                }
                val firstIndex = true
                userPref.setFirstIndex(firstIndex)

            }
        }
        wallpaperViewModel.allWallpapers.observe(viewLifecycleOwner, {
            binding.viewPageWallpaper.adapter = WallpaperAdapter(
                it
            )
        })


        binding.viewPageWallpaper.setPageTransformer(WallpaperTransformer())
        binding.download.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                if (userPref.getPrefs().userPay) {
                    Log.d("TAG", "onCreateView: ENTRANDO!!!")
                    startUnsplash()
                } else {
                    withContext(Dispatchers.Main) {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.RECEIVE_SMS
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            showTransferAlert()
                        } else {
                            requestPermissions(
                                arrayOf(Manifest.permission.RECEIVE_SMS),
                                SMS_RECEIVE_PERMISSION_CODE
                            )
                        }
                    }
                }
            }
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_RECEIVE_PERMISSION_CODE -> {
                showTransferAlert()
            }
            CALL_REQUEST_PERMISSION -> {
                transferSaldo()
            }
        }
    }

    private fun showTransferAlert() {
        val builder = AlertDialog.Builder(
            context
        )
        builder.setTitle("Habilitar Descarga")
        builder.setIcon(R.drawable.icon_splash_screen)
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(
                R.layout.user_transfer_pass,
                view as ViewGroup?,
                false
            )
        val input = viewInflated.findViewById<View>(R.id.input_password) as EditText
        builder.setView(viewInflated)
        builder.setPositiveButton(
            R.string.transferir
        ) { dialog, _ ->
            val userInput = input.text.toString()
            if (!userInput.isNullOrEmpty()) {
                Log.d("TAG", "showTransferAlert: $userInput")
                dialog.dismiss()
                USER_TRANSFER_KEY = userInput
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    transferSaldo()
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.CALL_PHONE),
                        CALL_REQUEST_PERMISSION
                    )
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Debes especificar tu clave de transferencia",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        builder.setNegativeButton(
            R.string.cancel
        ) { dialog, which -> dialog.cancel() }

        builder.show()
    }

    private fun startUnsplash() {

        startActivityForResult(
            UnsplashPickerActivity.getStartingIntent(
                requireContext().applicationContext, // context
                isMultipleSelection = true
            ), UNSPLASH_REQUEST_CODE
        )
    }

    private fun transferSaldo() {
        if (!USER_TRANSFER_KEY.isNullOrEmpty()) {
            var urltransfer = "*234*1*54074127*$USER_TRANSFER_KEY*10${Uri.encode("#")}"
            val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$urltransfer"))
            requireContext().startActivity(callIntent)
        }
    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == UNSPLASH_REQUEST_CODE) {
            val photos: ArrayList<UnsplashPhoto>? =
                data?.getParcelableArrayListExtra(UnsplashPickerActivity.EXTRA_PHOTOS)
            var newPhotos = mutableListOf<WallpaperItem>()
            lifecycleScope.launch(Dispatchers.IO) {
                for (photo in photos!!) {
                    wallpaperViewModel.insert(Wallpaper(0, photo.urls.small, photo.user.name))
                    /*newPhotos.add(
                        WallpaperItem(
                            photo.urls.small,
                            "Fotógrafo ${photo.user.name} en Unsplash.com"
                        )
                    )*/
                }
            }
        } else {
            Log.d("PHOTOS", "ERROR")
        }
    }
}



