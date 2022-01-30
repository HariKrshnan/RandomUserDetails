package com.hkay.zohouserdetails.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hkay.zohouserdetails.api.ApiHelper
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.User
import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel
import com.hkay.zohouserdetails.repo.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    val userDetailsResponse: MutableLiveData<ResponseModel> by lazy { MutableLiveData<ResponseModel>() }
    val weatherResponseModel: MutableLiveData<WeatherResponseModel> by lazy { MutableLiveData<WeatherResponseModel>() }
    val userDetailsFromDb: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>() }
    private val apiUtil: ApiHelper? = null
    var userResponse: ResponseModel? = null
    fun getUserDetails(dbHelper: DatabaseHelperImpl) {
        viewModelScope.launch {
            userResponse = mainRepository.getUsers(25)
            val usersList = mutableListOf<User>()
            val len = userResponse?.results?.size
            if (len != null)
                for (i in 0 until len) {
                    val user = userResponse?.results?.get(i)?.let { res ->
                        res.cell?.let { id ->
                            User(
                                id,
                                (res.name?.first + " " + res.name?.last),
                                res.picture?.medium,
                                res.location?.coordinates?.latitude,
                                res.location?.coordinates?.latitude
                            )
                        }
                    }
                    if (user != null) {
                        usersList.add(user)
                    }
                }
            dbHelper.insertAll(usersList)
            userDetailsResponse.postValue(userResponse)
        }
    }

    fun fetchDataFromDb(dbHelper: DatabaseHelperImpl) {
        viewModelScope.launch {
            userDetailsFromDb.postValue(dbHelper.getUsers())
        }
    }

    fun getWeatherDetails(lat: Int, long: Int) {
        viewModelScope.launch {
           val weatherResponse = apiUtil?.getWeatherInfo(lat, long)
            weatherResponseModel.postValue(weatherResponse)
        }
    }
}