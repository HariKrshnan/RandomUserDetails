package com.hkay.zohouserdetails.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hkay.zohouserdetails.R
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.User
import com.hkay.zohouserdetails.database.UserDatabase
import com.hkay.zohouserdetails.databinding.FragmentUserListBinding
import com.hkay.zohouserdetails.viewmodel.UserViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


class UserListFragment : Fragment(R.layout.fragment_user_list) {
    val mainActivity: MainActivity = MainActivity()
    private lateinit var userListBinding: FragmentUserListBinding
    @set: Inject
    var adapter: UserListAdapter? = null
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
        return userListBinding.root
    }

    private val viewModel: UserViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
//        fetchDataFromDb()
//        textChangeListener()
//        getWeatherData()
        getAllCharacters()
    }

    private fun getAllCharacters() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.charactersFlow.collectLatest { pagingData ->
                setUpRecyclerView()
                adapter?.submitData(pagingData)
            }
        }
    }

    private fun getWeatherData() {
        val bundle = arguments
        bundle?.getInt("latitude")
            ?.let { viewModel.getWeatherDetails(it, bundle.getInt("longitude")) }
        viewModel.weatherResponseModel.observe(viewLifecycleOwner, {
            userListBinding.toolbar.setTemperature(it.wind?.deg.toString())
            it.name?.let { it1 -> userListBinding.toolbar.setCity(it1) }
            it.weather?.firstOrNull()?.description?.let { it1 -> userListBinding.toolbar.setArea(it1) }
        })
    }

    private fun getUserDetails() {
        if (dbHelper != null) {
//            viewModel.getUserDetails(dbHelper)
        }
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
                list?.let {
//                    updateRecyclerView(it)
                                    }
            } else Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        })
    }

    private fun textChangeListener() {
        userListBinding.textField.editText?.doAfterTextChanged { text ->
            val searchList =
                list?.filter { it.name?.contains(text.toString(), ignoreCase = true) == true }
            if (searchList?.size == 0) Toast.makeText(activity, "No user found", Toast.LENGTH_SHORT)
                .show()
//            if (searchList != null) updateRecyclerView(searchList)
        }
    }

    private fun setUpRecyclerView() {
        userListBinding.userList.setHasFixedSize(true)
        _sGridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        userListBinding.userList.layoutManager = _sGridLayoutManager
        adapter = context?.let { it ->
            UserListAdapter(it) { user ->
                if (user != null) {
                    val bundle = bundleOf(
                        "pictureUrl" to user.image,
                        "userName" to user.name
//                        "latitude" to user.latitude,
//                        "longitude" to user.longitude
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
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.charactersFlow.collectLatest { pagingData ->
                adapter?.submitData(pagingData)
            }
        }
    }
}