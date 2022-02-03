package com.hkay.zohouserdetails.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.rick_n_morty.Characters
import com.hkay.zohouserdetails.model.weathermodel.WeatherResponseModel
import com.hkay.zohouserdetails.pagination.UsersPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getUsersInfo(results: Int): ResponseModel {
        return apiService.getUsersInfo()
    }

    override suspend fun getWeatherInfo(lat: Int, lon: Int): WeatherResponseModel {
        return apiService.getWeatherInfo(89, 70)
    }

    override suspend fun getAllCharacters(): Flow<PagingData<Characters>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { UsersPagingSource(apiService) }
    ).flow


}