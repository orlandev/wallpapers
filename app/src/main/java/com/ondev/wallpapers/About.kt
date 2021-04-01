package com.ondev.wallpapers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ondev.wallpapers.databinding.FragmentAboutBinding

class About : Fragment() {

    lateinit var binding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        FragmentAboutBinding.inflate(inflater, container, false).let { binding = it }

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