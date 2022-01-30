package com.hkay.zohouserdetails.repo

import com.hkay.zohouserdetails.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getUsers(usersCount: Int) = apiHelper.getUsersInfo(usersCount)
}