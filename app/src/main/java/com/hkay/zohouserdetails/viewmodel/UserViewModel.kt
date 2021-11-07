package com.hkay.zohouserdetails.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkay.zohouserdetails.core.ApiHelper
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.User
import com.hkay.zohouserdetails.model.ResponseModel
import kotlinx.coroutines.*

class UserViewModel: ViewModel() {
    val userDetailsResponse: MutableLiveData<ResponseModel> by lazy { MutableLiveData<ResponseModel>() }
    val userDetailsFromDb: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>() }
    private val apiHelper: ApiHelper by lazy { ApiHelper() }
    var userResponse: ResponseModel? = null
    fun getUserDetails(dbHelper: DatabaseHelperImpl){
        viewModelScope.launch {
            userResponse = apiHelper.getUserDetails()
            val users = mutableListOf<User>()
            val len = userResponse?.results?.size
            if (len != null)
             for (i in 0 until len) {
                 val user = userResponse?.results?.get(i)?.let { it.cell?.let { it1 -> User(it1, it.name?.first, it.name?.last) } }
                 if (user != null) {
                     users.add(user)
                 }
             }
            dbHelper.insertAll(users)
            userDetailsResponse.postValue(userResponse)
        }
    }

    fun fetchDataFromDb(dbHelper: DatabaseHelperImpl) {
        viewModelScope.launch  {
            userDetailsFromDb.postValue(dbHelper.getUsers())
        }
    }
}