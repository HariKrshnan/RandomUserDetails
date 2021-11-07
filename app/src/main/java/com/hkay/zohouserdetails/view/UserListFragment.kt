package com.hkay.zohouserdetails.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hkay.zohouserdetails.R
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.UserDatabase
import com.hkay.zohouserdetails.databinding.FragmentUserListBinding
import com.hkay.zohouserdetails.model.Result
import com.hkay.zohouserdetails.viewmodel.UserViewModel



class UserListFragment : Fragment(R.layout.fragment_user_list) {
    private var userListBinding: FragmentUserListBinding? = null
    private val binding get() = userListBinding!!
    private var adapter: UserListAdapter? = null
    private var _sGridLayoutManager: StaggeredGridLayoutManager? = null
    private var list: List<Result>? = null

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
        viewModel.userDetailsResponse.observe(viewLifecycleOwner, {
            if(it.results != null) {
                setUpRecyclerView(it.results)
            } else Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        })
        if (dbHelper != null) {
            viewModel.getUserDetails(dbHelper)
        }
    }

    private fun setUpRecyclerView(list: List<Result>) {
        userListBinding?.userList?.setHasFixedSize(true)
        _sGridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        userListBinding?.userList?.layoutManager = _sGridLayoutManager
        adapter = context?.let { it ->
            UserListAdapter(it) { results ->
                if (results != null) {
                    Toast.makeText(activity, results.email, Toast.LENGTH_SHORT).show()
                }
            }
        }
        userListBinding?.userList?.adapter = adapter
        Log.i("Test", "SetUpRecyclerView")
        updateRecyclerView(list)
    }

    private fun updateRecyclerView(list: List<Result>) {
        list[0].gender?.let { Log.i("Test", it) }
        adapter?.submitList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListBinding = null
    }
}