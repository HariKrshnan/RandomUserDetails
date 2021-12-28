package com.hkay.zohouserdetails.view

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.hkay.zohouserdetails.databinding.ActivityMainBinding.inflate
import com.hkay.zohouserdetails.databinding.DialogImagePreviewBinding
import com.hkay.zohouserdetails.databinding.UserListItemBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class ImagePreviewDialog : DialogFragment() {

    private var _binding: DialogImagePreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogImagePreviewBinding.inflate(LayoutInflater.from(context))
        activity?.let {
            Glide.with(it)
                .load(arguments?.getString("pictureUrl"))
                .fitCenter()
                .into(binding.previewImageView)
        }
        binding.mTVName.text = arguments?.getString("name")
        return AlertDialog.Builder(activity)
            .setView(binding.root)
            .create()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}