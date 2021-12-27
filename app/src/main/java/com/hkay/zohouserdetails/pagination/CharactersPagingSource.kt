package com.hkay.zohouserdetails.pagination

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hkay.zohouserdetails.core.ApiHelper
import com.hkay.zohouserdetails.core.ApiService
import com.hkay.zohouserdetails.model.rickandmorty.Characters

class CharactersPagingSource(
    private val service: ApiHelper
): PagingSource<Int, Characters>() {
    override fun getRefreshKey(state: PagingState<Int, Characters>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Characters> {
        val pageNumber = params.key ?: 1
        return try {
            Log.i("PagingSource", "Method call")
            val response = service.getAllCharacters(pageNumber)
            val pagedResponse = response?.body()
            val data = pagedResponse?.results

            var nextPageNumber: Int? = null
            if (pagedResponse?.pageInfo?.next != null) {
                val uri = Uri.parse(pagedResponse.pageInfo.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            Log.i("PagingSourceException", e.toString())
            LoadResult.Error(e)
        }
    }
}