package com.hkay.zohouserdetails.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hkay.zohouserdetails.R
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.User
import com.hkay.zohouserdetails.database.UserDatabase
import com.hkay.zohouserdetails.databinding.FragmentUserListBinding
import com.hkay.zohouserdetails.viewmodel.UserViewModel


class UserListFragment : Fragment(R.layout.fragment_user_list) {
    private var userListBinding: FragmentUserListBinding? = null
    private val binding get() = userListBinding!!
    private var adapter: UserListAdapter? = null
    private var _sGridLayoutManager: StaggeredGridLayoutManager? = null
    private var list: List<User>? = null
    private val dbHelper = context?.let { UserDatabase.DatabaseBuilder.getInstance(it) }?.let {
        DatabaseHelperImpl(
            it
        )
    }

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
        fetchDataFromDb()
        textChangeListener()
        scrollListener()
        binding.toolbar.setTitle("User title")
        binding.toolbar.setTemperature("31")
        binding.toolbar.setCity("Chennai")
        binding.toolbar.setArea("KK Nagar")
    }

    private fun getUserDetails() {
        if (dbHelper != null) {
            viewModel.getUserDetails(dbHelper)
        }
    }

    private fun scrollListener() {
        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                Toast.makeText(activity, "Pagination", Toast.LENGTH_SHORT).show()
                binding.loadingIndicator.visibility = View.VISIBLE
                getUserDetails()
                userDetailsObserver()
            }
        })
    }

    private fun userDetailsObserver() {
        viewModel.userDetailsResponse.observe(viewLifecycleOwner, {
            fetchDataFromDb()
            binding.loadingIndicator.visibility = View.GONE
        })
    }

    private fun fetchDataFromDb() {
        val dbHelper = context?.let { UserDatabase.DatabaseBuilder.getInstance(it) }?.let {
            DatabaseHelperImpl(
                it
            )
        }
        if (dbHelper != null) {
            viewModel.fetchDataFromDb(dbHelper)
        }
        viewModel.userDetailsFromDb.observe(viewLifecycleOwner, { user ->
            if (user != null) {
                list = user
                setUpRecyclerView()
                list?.let { updateRecyclerView(it) }
            } else Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        })
    }

    private fun textChangeListener() {
        binding.textField.editText?.doAfterTextChanged { text ->
            val searchList =
                list?.filter { it.name?.contains(text.toString(), ignoreCase = true) == true }
            if (searchList?.size == 0) Toast.makeText(activity, "No user found", Toast.LENGTH_SHORT)
                .show()
            if (searchList != null) updateRecyclerView(searchList)
        }
    }

    private fun setUpRecyclerView() {
        userListBinding?.userList?.setHasFixedSize(true)
        _sGridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        userListBinding?.userList?.layoutManager = _sGridLayoutManager
        adapter = context?.let { it ->
            UserListAdapter(it) { user ->
                if (user != null) {
                    val bundle = bundleOf(
                        "pictureUrl" to user.picture,
                        "userName" to user.name,
                        "latitude" to user.latitude,
                        "longitude" to user.longitude
                    )
                    findNavController().navigate(
                        R.id.action_userListFragment_to_userDetailFragment,
                        bundle
                    )
                }
            }
        }
        userListBinding?.userList?.adapter = adapter
        Log.i("Test", "SetUpRecyclerView")
    }

    private fun updateRecyclerView(list: List<User>) {
        adapter?.submitList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListBinding = null
    }
}