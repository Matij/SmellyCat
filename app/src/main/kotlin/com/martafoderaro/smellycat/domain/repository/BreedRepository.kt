package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage
import com.martafoderaro.smellycat.data.datasources.network.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface BreedRepository {
    suspend fun breeds(): Flow<ResultWrapper<List<Breed>>>
    suspend fun breed(breedId: String): Flow<Breed>
    suspend fun searchBreedImages(breedId: String, page: Int, limit: Int, order: String): Flow<ResultWrapper<List<BreedImage>>>
}