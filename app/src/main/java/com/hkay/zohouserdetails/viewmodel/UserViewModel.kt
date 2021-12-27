package com.hkay.zohouserdetails.viewmodel

import android.graphics.pdf.PdfDocument
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hkay.zohouserdetails.core.ApiHelper
import com.hkay.zohouserdetails.core.CharacterRepository
import com.hkay.zohouserdetails.core.CharacterRepositoryImpl
import com.hkay.zohouserdetails.database.DatabaseHelperImpl
import com.hkay.zohouserdetails.database.User
import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.rickandmorty.Characters
import com.hkay.zohouserdetails.model.rickandmorty.PagedResponse
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class UserViewModel : ViewModel() {
    val userDetailsResponse: MutableLiveData<ResponseModel> by lazy { MutableLiveData<ResponseModel>() }
    val rickyMortyResponseModel: MutableLiveData<List<Characters>> by lazy { MutableLiveData<List<Characters>>() }
    val weatherResponseModel: MutableLiveData<WeatherResponseModel> by lazy { MutableLiveData<WeatherResponseModel>() }
    val userDetailsFromDb: MutableLiveData<List<User>> by lazy { MutableLiveData<List<User>>() }
    private val apiHelper: ApiHelper by lazy { ApiHelper() }
    private val characterRepository: CharacterRepositoryImpl = CharacterRepositoryImpl(apiHelper)
    private lateinit var _charactersFlow: Flow<PagingData<Characters>>
    val charactersFlow: Flow<PagingData<Characters>>
        get() = _charactersFlow
    var userResponse: ResponseModel? = null
    fun getUserDetails() {
        getAllCharacters()
    }

    fun fetchDataFromDb(dbHelper: DatabaseHelperImpl) {
        viewModelScope.launch {
            userDetailsFromDb.postValue(dbHelper.getUsers())
        }
    }

    fun getWeatherDetails(lat: Int, long: Int) {
        viewModelScope.launch {
            val weatherResponse = apiHelper.getWeatherData(lat, long)
            weatherResponseModel.postValue(weatherResponse)
        }
    }

    private fun getAllCharacters() {
        viewModelScope.launch {
            val response = apiHelper.getAllCharacters(1)
            rickyMortyResponseModel.postValue(response?.body()?.results)
            characterRepository.getAllCharacters().cachedIn(viewModelScope).let {
                Log.i("TAG", "Method Call")
                _charactersFlow = it
            }
        }
    }

}