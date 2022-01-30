package com.hkay.zohouserdetails.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class ApiUtil {
     private val baseUrl = "https://randomuser.me/"
    private var service: ApiService? = null
     private val weatherUrl = "https://api.openweathermap.org/data/2.5/"
    init {
       getRetrofitBuilder()
    }
    fun getRetrofitBuilder(state: Int = 0) {
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
}