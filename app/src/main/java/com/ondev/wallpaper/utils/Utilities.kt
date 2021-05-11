package com.ondev.wallpaper.utils

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.facebook.shimmer.Shimmer
import com.ondev.wallpaper.R

fun shimmerSetup(): Shimmer.AlphaHighlightBuilder {
    val shimmerBuilder = Shimmer.AlphaHighlightBuilder()
    shimmerBuilder.setDuration(2000L).setRepeatMode(ValueAnimator.INFINITE)
    shimmerBuilder.setBaseAlpha(0.7f)
    return shimmerBuilder
}

fun ShareIt(context: Context) {
    val bm = BitmapFactory.decodeResource(
        context.resources,
        R.drawable.share_app
    )
    val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
    intent.putExtra(
        Intent.EXTRA_TEXT,
        context.resources.getString(R.string.app_share_text)
    )
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bm, "", null)
    val screenshotUri: Uri = Uri.parse(path)
    intent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
    intent.type = "image/*"
    context.startActivity(
        Intent.createChooser(
            intent,
            context.resources.getString(R.string.share_by)
        )
    )
}
