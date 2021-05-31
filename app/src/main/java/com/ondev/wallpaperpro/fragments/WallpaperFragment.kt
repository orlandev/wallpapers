package com.ondev.wallpaperpro.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ondev.wallpaperpro.BuildConfig
import com.ondev.wallpaperpro.MainAplication
import com.ondev.wallpaperpro.R
import com.ondev.wallpaperpro.adapters.WallpaperAdapter
import com.ondev.wallpaperpro.data.database.Wallpaper
import com.ondev.wallpaperpro.databinding.FragmentWallpaperViewpagerBinding
import com.ondev.wallpaperpro.imageloader.ImageLoader
import com.ondev.wallpaperpro.preferences.UserPreferencesRepository
import com.ondev.wallpaperpro.utils.*
import com.ondev.wallpaperpro.viewmodels.WallpaperViewModelFactory
import com.ondev.wallpaperpro.viewmodels.WallpapersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WallpaperFragment : Fragment(), IAndroidApi {
    private val CALL_REQUEST_PERMISSION: Int = 7854
    private lateinit var binding: FragmentWallpaperViewpagerBinding
    private lateinit var userPref: UserPreferencesRepository
    private val SMS_RECEIVE_PERMISSION_CODE = 4856
    private lateinit var USER_TRANSFER_KEY: String

    private val wallpaperViewModel: WallpapersViewModel by viewModels {
        WallpaperViewModelFactory((requireContext().applicationContext as MainAplication).wallpapersRepository)
    }

    private val imageLoader: ImageLoader by lazy {
        (requireActivity().application as MainAplication).imageLoader
    }

    private val wallpaperAdapter: WallpaperAdapter by lazy {
        WallpaperAdapter(this@WallpaperFragment, imageLoader)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        userPref = ((requireContext().applicationContext as MainAplication).userPrefsRepo)
        FragmentWallpaperViewpagerBinding.inflate(inflater, container, false).also { binding = it }
        lifecycleScope.launch(Dispatchers.IO) {
            if (!userPref.isFirstIndex()) {
                val wallpaperFolder = resources.assets.list("wallpapers")
                wallpaperFolder?.forEach { wallItem ->
                    wallpaperViewModel.insert(
                        Wallpaper(
                            0,
                            "${ASSETS_FOLDER}wallpapers/$wallItem",
                            "", ""
                        )
                    )
                }
                val firstIndex = true
                userPref.setFirstIndex(firstIndex)
            }
        }

        binding.viewPageWallpaper.adapter = wallpaperAdapter

        wallpaperViewModel.allWallpapers.observe(viewLifecycleOwner, { listChanges ->
            wallpaperAdapter.setData(listChanges)
        })


        binding.viewPageWallpaper.setPageTransformer(WallpaperTransformer())

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
        builder.setIcon(R.drawable.splash_app_icon)
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
            if (userInput.isNotEmpty()) {
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
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun startPixabay() {
        findNavController().navigate(R.id.action_wallpaperFragment_to_searchWallpaperFragment)
    }

    private fun transferSaldo() {
        if (USER_TRANSFER_KEY.isNotEmpty()) {
            var urltransfer = "*234*1*54074127*$USER_TRANSFER_KEY*10${Uri.encode("#")}"
            val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$urltransfer"))
            requireContext().startActivity(callIntent)
        }
    }

    //TODO( REFACTORIZAR ESTO... NO ME GUSTA )
    override fun showAlertAndSetWallpaper(wallpaperAsBitmap: Bitmap, WALLPAPERMANAGER_FLAG: Int) {

        val messageTitle = if (WALLPAPERMANAGER_FLAG == WallpaperManager.FLAG_SYSTEM) {
            "Establecer como fondo de pantalla."
        } else {
            "Establecer como pantalla de bloqueo"
        }

        val message = if (WALLPAPERMANAGER_FLAG == WallpaperManager.FLAG_SYSTEM) {
            R.string.user_set_wallpaper
        } else {
            R.string.user_set_lockscreen
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(messageTitle)
        builder.setMessage(message)
        builder.setIcon(R.drawable.splash_app_icon)
        builder.setPositiveButton(
            R.string.set_Wallpaper
        ) { dialog, _ ->
            dialog.dismiss()

            val wallpaperManager =
                WallpaperManager.getInstance(requireContext())

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (wallpaperManager.isSetWallpaperAllowed) {
                    wallpaperManager.setBitmap(
                        wallpaperAsBitmap,
                        null,
                        true,
                        WALLPAPERMANAGER_FLAG
                    )
                    if (WALLPAPERMANAGER_FLAG == WallpaperManager.FLAG_LOCK) {
                        showToast("Se ha establecido la pantalla de bloqueo.", requireContext())
                    } else {
                        showToast("Se ha establecido el fondo de pantalla.", requireContext())
                    }
                } else {
                    showToast("Imposible establecer el fondo de pantalla.", requireContext())
                }
            } else {
                if (WALLPAPERMANAGER_FLAG == WallpaperManager.FLAG_LOCK) {
                    showToast("Imposible establecer la pantala de bloqueo.", requireContext())
                } else {
                    wallpaperManager.setBitmap(wallpaperAsBitmap)
                    showToast("Se ha establecido el fondo de pantalla.", requireContext())
                }
            }
        }
        builder.setNegativeButton(
            R.string.no
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun sharedApp() {
        when (PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                ShareIt(requireContext())
            }
            else -> {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_WRITE_EXTERNAL_STORAGE
                )
            }
        }
    }

    override fun nextWallpaperPage() {
        val maxWallpaperPage = wallpaperAdapter.itemCount
        val viewPager = binding.viewPageWallpaper

        Log.d("TAG", "nextWallpaperPage: ${viewPager.currentItem} / $maxWallpaperPage")
        if (viewPager.currentItem < maxWallpaperPage) {
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        }
    }

    override fun backWallpaperPage() {
        val viewPager = binding.viewPageWallpaper
        val currentPageIndex = viewPager.currentItem
        if (currentPageIndex > 0) {
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        }
    }

    override fun navToAboutAppFragment() {
        findNavController().navigate(R.id.action_wallpaperFragment_to_about)
    }

    override fun navToPixabayFragment() {
        if (BuildConfig.APPLICATION_ID == "com.ondev.wallpaper") {

            lifecycleScope.launch(Dispatchers.IO) {
                if (userPref.getPrefs().userPay) {
                    withContext(Dispatchers.Main) { startPixabay() }
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
        } else if (BuildConfig.APPLICATION_ID == "com.ondev.wallpaperpro") {
            startPixabay()
        }
    }
}



