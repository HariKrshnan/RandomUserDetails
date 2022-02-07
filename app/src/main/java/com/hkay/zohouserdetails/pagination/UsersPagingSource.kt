package com.hkay.zohouserdetails.pagination

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hkay.zohouserdetails.api.ApiService
import com.hkay.zohouserdetails.model.rick_n_morty.Characters
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UsersPagingSource
@Inject constructor(private val apiHelper: ApiService) : PagingSource<Int, Characters>() {
    override fun getRefreshKey(state: PagingState<Int, Characters>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Characters> {
        val pageNumber = params.key ?: 1
        return try {
            val response = apiHelper.getAllCharacters(pageNumber)
            val result = response.body()
            val data = result?.results
            var nextPageNumber: Int? = null
            if (result?.pageInfo?.next != null) {
                val uri = Uri.parse(result.pageInfo.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
                Log.i("NEWPage", "new page loaded $nextPageNumber")
            }
            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = nextPageNumber
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}