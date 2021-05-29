package com.ondev.wallpaper.adapters

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.facebook.shimmer.ShimmerFrameLayout
import com.ondev.wallpaper.R
import com.ondev.wallpaper.data.database.Wallpaper
import com.ondev.wallpaper.utils.PERMISSION_WRITE_EXTERNAL_STORAGE
import com.ondev.wallpaper.utils.ShareIt
import com.ondev.wallpaper.utils.shimmerSetup


class WallpaperAdapter(
    private var wallpaperItems: List<Wallpaper>
) : RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {
    inner class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageWallpaper: ImageView = itemView.findViewById(R.id.image_view_parallax_effect)
        val backWallpaper: ImageView = itemView.findViewById(R.id.back_wallpaper)
        val nextWallpaper: ImageView = itemView.findViewById(R.id.next_wallpaper)
        val setWallpaper: ImageView = itemView.findViewById(R.id.set_wallpaper)
        val setLockScreenWallpaper: ImageView = itemView.findViewById(R.id.set_lock_screen)
        val shimmerLayer: ShimmerFrameLayout = itemView.findViewById(R.id.shimmer_view_container)
        val shareApp: ImageView = itemView.findViewById(R.id.share_app)
        val showPhotoCount: TextView = itemView.findViewById(R.id.photo_count)
        val aboutBtn: ImageView = itemView.findViewById(R.id.about_app)
        val photoOwner: TextView = itemView.findViewById(R.id.photo_owner)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WallpaperViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_wallpaper_screen, parent, false)
        return WallpaperViewHolder(
            view
        )
    }


    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val currentWallpaperItem = wallpaperItems[position]
        holder.shimmerLayer.setShimmer(shimmerSetup().build())
        if (!currentWallpaperItem.owner.isNullOrEmpty()) {
            holder.photoOwner.text = "Fotógrafo ${currentWallpaperItem.owner}"
        } else {
            holder.photoOwner.visibility = View.INVISIBLE
        }
        holder.showPhotoCount.text = "${position + 1} / ${wallpaperItems.size}"
        holder.aboutBtn.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_wallpaperFragment_to_about)
        }

        Glide.with(holder.imageWallpaper.context)
            .load(Uri.parse(currentWallpaperItem.url))
            .placeholder(R.drawable.download)
            .centerCrop()
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

        holder.setWallpaper.setOnClickListener(View.OnClickListener { view ->
            showAlertDialogSetWallpaper(holder, view, WallpaperManager.FLAG_SYSTEM)
        })

        holder.setLockScreenWallpaper.setOnClickListener { view ->
            showAlertDialogSetWallpaper(holder, view, WallpaperManager.FLAG_LOCK)
        }

        holder.shareApp.setOnClickListener(View.OnClickListener {
            shareApp(it.context)
        })
    }


    private fun showAlertDialogSetWallpaper(holder: WallpaperViewHolder, view: View, wich: Int) {
        val messageTitle = if (wich == WallpaperManager.FLAG_SYSTEM) {
            "Establecer como fondo de pantalla."
        } else {
            "Establecer como pantalla de bloqueo"
        }

        val message = if (wich == WallpaperManager.FLAG_SYSTEM) {
            R.string.user_set_wallpaper
        } else {
            R.string.user_set_lockscreen
        }

        val builder = AlertDialog.Builder(view.context)
        builder.setTitle(messageTitle)
        builder.setMessage(message)
        builder.setIcon(R.drawable.icon_splash_screen)
        builder.setPositiveButton(
            R.string.set_Wallpaper
        ) { dialog, _ ->
            dialog.dismiss()

            val wallpaperManager =
                WallpaperManager.getInstance(view.context.applicationContext)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (wallpaperManager.isSetWallpaperAllowed) {
                    wallpaperManager.setBitmap(
                        holder.imageWallpaper.drawable.toBitmap(),
                        null,
                        true,
                        wich
                    )
                    if (wich == WallpaperManager.FLAG_LOCK) {
                        showToast("Se ha establecido la pantalla de bloqueo.", view.context)
                    } else {
                        showToast("Se ha establecido el fondo de pantalla.", view.context)
                    }
                } else {
                    showToast("Imposible establecer el fondo de pantalla.", view.context)
                }
            } else {
                if (wich == WallpaperManager.FLAG_LOCK) {
                    showToast("Imposible establecer la pantala de bloqueo.", view.context)
                } else {
                    wallpaperManager.setBitmap(holder.imageWallpaper.drawable.toBitmap())
                    showToast("Se ha establecido el fondo de pantalla.", view.context)
                }
            }


        }
        builder.setNegativeButton(
            R.string.no
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
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

    private fun showToast(message: String, ctx: Context) {
        var toast =
            Toast.makeText(
                ctx,
                message,
                Toast.LENGTH_SHORT
            )
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    override fun getItemCount(): Int {
        return wallpaperItems.size
    }
}