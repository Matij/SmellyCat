package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage
import kotlinx.coroutines.flow.Flow

interface BreedRepository {
    fun breeds(): Flow<List<Breed>>
    fun breed(breedId: String): Flow<Breed>
    fun searchBreedImages(breedId: String, page: Int, limit: Int, order: String): Flow<List<BreedImage>>
}