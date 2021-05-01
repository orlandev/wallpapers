package com.ondev.wallpaper

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WallpaperAdapter(

    private var wallpaperItems: List<WallpaperItem>


) : RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {


    inner class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageWallpaper: ImageView = itemView.findViewById(R.id.image_view_parallax_effect)
        val backWallpaper: ImageView = itemView.findViewById(R.id.back_wallpaper)
        val nextWallpaper: ImageView = itemView.findViewById(R.id.next_wallpaper)
        val setWallpaper: ImageView = itemView.findViewById(R.id.set_wallpaper)
        val shimmerLayer: ShimmerFrameLayout = itemView.findViewById(R.id.shimmer_view_container)
        val shareApp: ImageView = itemView.findViewById(R.id.share_app)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WallpaperViewHolder {
        return WallpaperViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_wallpaper_screen, parent, false)
        )
    }


    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {

        holder.shimmerLayer.setShimmer(shimmerSetup().build())

        val currentWallpaperItem = wallpaperItems[position]
        Glide.with(holder.imageWallpaper.context)
            .load(Uri.parse(currentWallpaperItem.wallpaperFileName))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageWallpaper)

        holder.backWallpaper.setOnClickListener(View.OnClickListener {
            var viewPager =
                (it.context as Activity).findViewById<ViewPager2>(R.id.view_page_wallpaper)
            val currentPageIndex = viewPager.currentItem
            if (currentPageIndex > 0)
                viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        })
        holder.nextWallpaper.setOnClickListener(View.OnClickListener {
            var viewPager =
                (it.context as Activity).findViewById<ViewPager2>(R.id.view_page_wallpaper)
            if (viewPager.currentItem < wallpaperItems.size)
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        })

        holder.setWallpaper.setOnClickListener(View.OnClickListener {
            GlobalScope.launch {
                setWallpaper(it, holder.imageWallpaper.drawable.toBitmap())
            }
            var toast =
                Toast.makeText(
                    it.context,
                    "Se ha cambiado el fondo de pantalla.",
                    Toast.LENGTH_SHORT
                )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()

        })

        holder.shareApp.setOnClickListener(View.OnClickListener {
            shareApp(it.context)
        })
    }

    private fun setWallpaper(view: View, bitmap: Bitmap) {

        val wallpaperManager =
            WallpaperManager.getInstance(view.context.applicationContext)
        wallpaperManager.setBitmap(bitmap)

    }


    private fun shareApp(context: Context) = when (PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) -> {
            ShareIt(context)
        }
        else -> {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_WRITE_EXTERNAL_STORAGE
            )
        }
    }


    override fun getItemCount(): Int {
        return wallpaperItems.size
    }
}