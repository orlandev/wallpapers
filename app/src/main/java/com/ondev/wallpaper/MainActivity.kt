package com.ondev.wallpaper

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ondev.wallpaper.utils.ShareIt
import cu.uci.apklisupdate.ApklisUpdate
import cu.uci.apklisupdate.UpdateCallback
import cu.uci.apklisupdate.model.AppUpdateInfo
import cu.uci.apklisupdate.view.ApklisUpdateDialog
import cu.uci.apklisupdate.view.ApklisUpdateFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Wallpapers)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }

        ApklisUpdate.hasAppUpdate(this, callback = object : UpdateCallback {
            override fun onNewUpdate(appUpdateInfo: AppUpdateInfo) {

                //Start info alert dialog or do what you want.
                ApklisUpdateDialog(
                    this@MainActivity,
                    appUpdateInfo,
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.colorAccent
                    )
                ).show()

                //Start info fragment or do what you want.
                supportFragmentManager.beginTransaction().add(
                    R.id.container, ApklisUpdateFragment.newInstance(
                        updateInfo = appUpdateInfo,
                        actionsColor = ContextCompat.getColor(
                            this@MainActivity,
                            R.color.colorAccent
                        )
                    )
                ).commit()

            }

            override fun onOldUpdate(appUpdateInfo: AppUpdateInfo) {
                Log.d("MainActivity", "onOldUpdate $appUpdateInfo")
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    ShareIt(this)
                }
            }
        }
    }
}