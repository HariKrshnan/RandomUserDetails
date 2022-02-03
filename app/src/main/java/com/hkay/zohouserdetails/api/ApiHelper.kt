package com.hkay.zohouserdetails.api

import androidx.paging.PagingData
import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.rick_n_morty.Characters
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel
import kotlinx.coroutines.flow.Flow

interface ApiHelper {
    suspend fun getUsersInfo(results: Int): ResponseModel
    suspend fun getWeatherInfo(lat: Int, lon: Int): WeatherResponseModel
    suspend fun getAllCharacters(): Flow<PagingData<Characters>>
}