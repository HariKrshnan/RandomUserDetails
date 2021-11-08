package com.hkay.zohouserdetails.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
            } else Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        })
    }

    private fun textChangeListener() {
        binding.textField.editText?.doAfterTextChanged { text ->
            val searchList = list?.filter { it.name?.contains(text.toString(), ignoreCase = true) == true }
            if (searchList != null) {
                updateRecyclerView(searchList)
            }
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
                    Toast.makeText(activity, user.name, Toast.LENGTH_SHORT).show()
                }
            }
        }
        userListBinding?.userList?.adapter = adapter
        Log.i("Test", "SetUpRecyclerView")
        list?.let { updateRecyclerView(it) }
    }

    private fun updateRecyclerView(list: List<User>) {
        adapter?.submitList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListBinding = null
    }
}