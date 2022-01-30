package com.hkay.zohouserdetails.api

import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel

interface ApiHelper {
    suspend fun getUsersInfo(results: Int): ResponseModel
    suspend fun getWeatherInfo(lat: Int, lon: Int): WeatherResponseModel
}