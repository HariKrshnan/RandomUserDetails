package com.hkay.zohouserdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.hkay.zohouserdetails.R
import com.hkay.zohouserdetails.databinding.FragmentUserDetailBinding

class UserDetailFragment : Fragment(R.layout.fragment_user_detail) {
    private lateinit var binding: FragmentUserDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        val bundle = arguments
        initView(bundle)
    }

    private fun initView(bundle: Bundle?) {
      binding.userName.text = bundle?.getString("userName")
        activity?.let {
            Glide.with(it)
                .load(bundle?.getString("pictureUrl"))
                .fitCenter()
                .into(binding.userImage)
        }
    }
}