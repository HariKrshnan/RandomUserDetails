package com.hkay.zohouserdetails.core

import com.hkay.zohouserdetails.model.ResponseModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("api/?results=25")
    suspend fun getUsersInfo(): ResponseModel
}