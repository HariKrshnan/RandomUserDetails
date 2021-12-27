package com.hkay.zohouserdetails.core

import com.hkay.zohouserdetails.model.rickandmorty.Characters
import com.hkay.zohouserdetails.model.rickandmorty.PagedResponse
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response


class ApiHelper {
     private val baseUrl = "https://randomuser.me/"
    private var service: ApiService? = null
     private val weatherUrl = "https://api.openweathermap.org/data/2.5/"
    private val rickAndMortyUrl = "https://rickandmortyapi.com/api/"
    init {
       getRetrofitBuilder()
    }
    private fun getRetrofitBuilder(state: Int = 0) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(if (state == 0) rickAndMortyUrl else weatherUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retrofit.create(ApiService::class.java)
    }

    suspend fun getWeatherData(lat: Int, long: Int): WeatherResponseModel? {
        getRetrofitBuilder(1)
        return  service?.getWeatherInfo(lat, long)
    }
    suspend fun getAllCharacters(page: Int): Response<PagedResponse<Characters>>? {
        getRetrofitBuilder()
      return service?.getAllCharacters(page)
    }
}