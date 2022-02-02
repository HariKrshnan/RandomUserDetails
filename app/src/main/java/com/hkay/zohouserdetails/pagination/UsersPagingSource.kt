package com.hkay.zohouserdetails.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hkay.zohouserdetails.api.ApiHelper
import com.hkay.zohouserdetails.model.ResponseModel
import com.hkay.zohouserdetails.model.Result
import retrofit2.HttpException
import java.io.IOException

class UsersPagingSource(
    private val apiHelper: ApiHelper,
    private val count: Int
) : PagingSource<Int, Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val position = params.key ?: 1
        return try {
           val response = apiHelper.getUsersInfo(count)
           val result = response.results
            val nextKey = if (result.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / 30)
            }
            LoadResult.Page(
                data = result,
                prevKey = if (position == 1) null else position - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}