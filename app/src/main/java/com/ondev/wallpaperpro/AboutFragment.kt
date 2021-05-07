package com.ondev.wallpaperpro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ondev.wallpaperpro.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    lateinit var binding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        FragmentAboutBinding.inflate(inflater, container, false).let { binding = it }

        binding.closeAbout.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_about_to_wallpaperFragment)
        })

        binding.appDeveloper.setOnClickListener(View.OnClickListener {
            openUri(
                requireContext(),
                resources.getString(R.string.github_url),
                TypeUriSocialMedia.URL
            )
        })
        return binding.root
    }

}