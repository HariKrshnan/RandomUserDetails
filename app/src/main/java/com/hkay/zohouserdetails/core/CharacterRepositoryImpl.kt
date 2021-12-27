package com.hkay.zohouserdetails.core

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hkay.zohouserdetails.model.rickandmorty.Characters
import com.hkay.zohouserdetails.pagination.CharactersPagingSource
import kotlinx.coroutines.flow.Flow

class CharacterRepositoryImpl(
    private val service: ApiHelper,
) {
    fun getAllCharacters(): Flow<PagingData<Characters>> = Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { CharactersPagingSource(service) }
        ).flow
}