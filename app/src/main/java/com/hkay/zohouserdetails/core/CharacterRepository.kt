package com.hkay.zohouserdetails.core

import androidx.paging.PagingData
import com.hkay.zohouserdetails.model.rickandmorty.Characters
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharacters(): Flow<PagingData<Characters>>
}