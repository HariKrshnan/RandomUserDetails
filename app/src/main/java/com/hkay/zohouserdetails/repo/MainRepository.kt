package com.hkay.zohouserdetails.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hkay.zohouserdetails.api.ApiHelper
import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.Result
import com.hkay.zohouserdetails.pagination.UsersPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun getUsers(usersCount: Int) = apiHelper.getUsersInfo(usersCount)

    fun getUsersStream(count: Int): Flow<PagingData<Result>> {
        return Pager(
            config = PagingConfig(pageSize = count, enablePlaceholders = false),
            pagingSourceFactory = { UsersPagingSource(apiHelper, count) }
        ).flow
    }
}