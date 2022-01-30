package com.hkay.zohouserdetails.api

import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getUsersInfo(results: Int): ResponseModel {
//        apiUtil.getRetrofitBuilder()
        return apiService.getUsersInfo()
    }

    override suspend fun getWeatherInfo(lat: Int, lon: Int): WeatherResponseModel {
//        apiUtil.getRetrofitBuilder(1)
        return apiService.getWeatherInfo(89, 70)
    }
}