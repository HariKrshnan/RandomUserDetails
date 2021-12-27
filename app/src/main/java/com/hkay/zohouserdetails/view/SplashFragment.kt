package com.hkay.zohouserdetails.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hkay.zohouserdetails.R
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.UserDatabase
import com.hkay.zohouserdetails.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment(R.layout.fragment_splash) {
    private val viewModel by viewModels<UserViewModel>()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var latitude = 0
    private var longitude = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.addFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    override fun onStart() {
        super.onStart()
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
//        if (!checkPermissions()) {
//            requestPermissions()
//        } else {
//            getLastLocation()
//        }
        view?.findNavController()
            ?.navigate(R.id.action_splashFragment_to_userListFragment)
//        viewModel.userDetailsResponse.observe(viewLifecycleOwner, {
//            if (it.results != null) {
//                lifecycleScope.launch {
//                    delay(5000)
//                    val bundle = bundleOf(
//                        "latitude" to latitude,
//                        "longitude" to longitude
//                    )
//                    view?.findNavController()
//                        ?.navigate(R.id.action_splashFragment_to_userListFragment, bundle)
//                }
//            } else Toast.makeText(activity, "No Internet", Toast.LENGTH_SHORT).show()
//        })
        val dbHelper = context?.let { UserDatabase.DatabaseBuilder.getInstance(it) }?.let {
            DatabaseHelperImpl(
                it
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        activity?.let {
            fusedLocationClient?.lastLocation?.addOnCompleteListener(it) { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result
                    latitude = lastLocation!!.latitude.toInt()
                    longitude = lastLocation!!.longitude.toInt()
                } else {
                    Toast.makeText(
                        activity,
                        "No location detected. Make sure location is enabled on the device.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun showToast(
        mainTextStringId: String
    ) {
        Toast.makeText(activity, mainTextStringId, Toast.LENGTH_LONG).show()
    }

    private fun checkPermissions(): Boolean {
        val permissionState = activity?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun requestPermissions() {
        val shouldProvideRationale = activity?.let {
            ActivityCompat.shouldShowRequestPermissionRationale(
                it,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
        if (shouldProvideRationale == true) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showToast("Location permission is needed for core functionality")
            startLocationPermissionRequest()
        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showToast("Permission was denied")
                    // Build intent that displays the App settings screen.
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts(
                        "package",
                        Build.DISPLAY, null
                    )
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        private const val TAG = "LocationProvider"
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}

