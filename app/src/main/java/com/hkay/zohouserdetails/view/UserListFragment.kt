package com.hkay.zohouserdetails.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hkay.zohouserdetails.R
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.UserDatabase
import com.hkay.zohouserdetails.databinding.FragmentUserListBinding
import com.hkay.zohouserdetails.viewmodel.UserViewModel


class UserListFragment : Fragment(R.layout.fragment_user_list) {
    private var userListBinding: FragmentUserListBinding? = null
    private val binding get() = userListBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userListBinding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel by viewModels<UserViewModel>()
    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        val dbHelper = context?.let { UserDatabase.DatabaseBuilder.getInstance(it) }?.let {
            DatabaseHelperImpl(
                it
            )
        }
        if (dbHelper != null) {
            viewModel.fetchDataFromDb(dbHelper)
        }
        viewModel.userDetailsFromDb.observe(viewLifecycleOwner, {list ->
           binding.textView.text = list[0].firstName + list[0].lastName
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListBinding = null
    }
}