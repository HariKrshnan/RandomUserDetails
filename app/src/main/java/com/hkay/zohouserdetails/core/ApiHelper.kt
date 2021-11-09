package com.hkay.zohouserdetails.core

import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class ApiHelper {
     private val baseUrl = "https://randomuser.me/"
    private var service: ApiService? = null
     private val weatherUrl = "https://api.openweathermap.org/data/2.5/"
    init {
       getRetrofitBuilder()
    }
    private fun getRetrofitBuilder(state: Int = 0) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(if (state == 0) baseUrl else weatherUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retrofit.create(ApiService::class.java)
    }
    suspend fun getUserDetails(): ResponseModel? {
        getRetrofitBuilder()
        return service?.getUsersInfo()
    }
    suspend fun getWeatherData(): WeatherResponseModel? {
        getRetrofitBuilder(1)
        return  service?.getWeatherInfo()
    }
}