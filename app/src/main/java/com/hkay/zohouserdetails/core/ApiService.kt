package com.hkay.zohouserdetails.core

import com.hkay.zohouserdetails.model.ResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api")
    suspend fun getUsersInfo(
        @Query("result")result: Int = 25
    ): ResponseModel
}