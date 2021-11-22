package com.hkay.zohouserdetails.core

import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getUsersInfo(
        @Query("results")results: Int = 25
    ): ResponseModel

    @GET("weather")
    suspend fun getWeatherInfo(
        @Query("lat")lat: Int,
        @Query("lon")lon: Int,
        @Query("appid")apiKey: String = "38781e38750a335dd868104f722abf5d",
    ): WeatherResponseModel
}