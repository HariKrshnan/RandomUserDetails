package com.hkay.zohouserdetails.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hkay.zohouserdetails.R
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.UserDatabase
import com.hkay.zohouserdetails.databinding.FragmentUserListBinding
import com.hkay.zohouserdetails.model.rickandmorty.Characters
import com.hkay.zohouserdetails.viewmodel.UserViewModel


class UserListFragment : Fragment(R.layout.fragment_user_list) {
    private var userListBinding: FragmentUserListBinding? = null
    private val binding get() = userListBinding!!
    private var adapter: UserListAdapter? = null
    private var _sGridLayoutManager: StaggeredGridLayoutManager? = null
    private var list: List<Characters>? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textChangeListener()
        getUserDetails()
        getWeatherData()
        viewModel.rickyMortyResponseModel.observe(viewLifecycleOwner, {
            binding.loadingIndicator.visibility = View.GONE
            setUpRecyclerView()
            updateRecyclerView(it)
            list = it
        })
    }

    private fun getWeatherData() {
        val bundle = arguments
        viewModel.getWeatherDetails(76, 89)
//        bundle?.getInt("latitude")?.let {
//            viewModel.getWeatherDetails(89, 89) }
        viewModel.weatherResponseModel.observe(viewLifecycleOwner, {
            binding.toolbar.setTemperature(it.wind?.deg.toString())
            it.name?.let { it1 -> binding.toolbar.setCity(it1) }
            it.weather?.firstOrNull()?.description?.let { it1 -> binding.toolbar.setArea(it1) }
        })
    }

    private fun getUserDetails() {
        binding.loadingIndicator.visibility = View.VISIBLE
        viewModel.getUserDetails()
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
        viewModel.userDetailsFromDb.observe(viewLifecycleOwner, {

        })
    }

    private fun textChangeListener() {
        binding.textField.editText?.doAfterTextChanged { text ->
            val searchList =
                list?.filter { it.name.contains(text.toString(), ignoreCase = true) }
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
        adapter = context?.let {
            UserListAdapter(it).apply {
                clickListener = { user ->
                    val bundle = bundleOf(
                        "pictureUrl" to user?.image,
                        "userName" to user?.name,
                        "latitude" to 89,
                        "longitude" to 89
                    )
                    findNavController().navigate(
                        R.id.action_userListFragment_to_userDetailFragment, bundle
                    )
                }
                longClickListener = { user ->
                    val dialogFragment = ImagePreviewDialog()
                    val args = Bundle()
                    if (user != null) {
                        args.putString("pictureUrl", user.image)
                        args.putString("name", user.name)
                    }
                    dialogFragment.arguments = args
                    dialogFragment.show(childFragmentManager, "Dialog")
                    true
                }
            }
        }

        userListBinding?.userList?.adapter = adapter
        Log.i("Test", "SetUpRecyclerView")
    }

    private fun updateRecyclerView(list: List<Characters>) {
        adapter?.submitList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userListBinding = null
    }
}