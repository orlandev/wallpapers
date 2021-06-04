package com.ondev.wallpaper.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat


/*
* Load a Social Media URL
*
* This function open an Intent depending of TypeUriSocialMedia
*
* TypeUriSocialMedia
*    URL -> open the specific url, for exmple if you intent open fcebook.com
*           and the facebook app is installed the sistem open the app insted the browser
*   PHONE-> open the phone caller and make a call
*
*   EMAIL -> open the email app and add data to send email
*
* */
fun openUri(context: Context, urlSocialMedia: String, type: TypeUriSocialMedia) {
    when (type) {
      TypeUriSocialMedia.URL -> {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(urlSocialMedia)
        context.startActivity(intent)
      }
      TypeUriSocialMedia.PHONE -> {
        try {
          if (Build.VERSION.SDK_INT > 22) {
            if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
              ) != PackageManager.PERMISSION_GRANTED
            ) {
              ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                101
              )
              return
            }
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$urlSocialMedia")
            context.startActivity(callIntent)
          } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$urlSocialMedia")
            context.startActivity(callIntent)
          }
        } catch (ex: Exception) {
          ex.printStackTrace()
        }
      }
      else -> {

      }
    }
}