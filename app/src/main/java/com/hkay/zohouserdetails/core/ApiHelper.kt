package com.hkay.zohouserdetails.core

import com.hkay.zohouserdetails.model.ResponseModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class ApiHelper {
    private val baseUrl = "https://randomuser.me/"
    private val service: ApiService
    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        service = retrofit.create(ApiService::class.java)
    }

    suspend fun getUserDetails(): ResponseModel {
       return service.getUsersInfo()
    }
}