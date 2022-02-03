package com.hkay.zohouserdetails.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hkay.zohouserdetails.api.ApiHelper
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.User
import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.rick_n_morty.Characters
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val apiHelper: ApiHelper) : ViewModel() {
    val userDetailsResponse: MutableLiveData<ResponseModel> by lazy { MutableLiveData<ResponseModel>() }
    val weatherResponseModel: MutableLiveData<WeatherResponseModel> by lazy { MutableLiveData<WeatherResponseModel>() }
    val userDetailsFromDb: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>() }
    private val apiUtil: ApiHelper? = null
    var userResponse: ResponseModel? = null
    private lateinit var _charactersFlow: Flow<PagingData<Characters>>
    val charactersFlow: Flow<PagingData<Characters>>
        get() = _charactersFlow

    //Fetching and storing in DB
    fun getUserDetails(dbHelper: DatabaseHelperImpl) {
        viewModelScope.launch {
            //userResponse = mainRepository.getUsers(25)
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
    init {
        getAllCharacters()
    }
    //Pagination call
    private fun getAllCharacters() = viewModelScope.launch {
        _charactersFlow = apiHelper.getAllCharacters().cachedIn(viewModelScope)
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