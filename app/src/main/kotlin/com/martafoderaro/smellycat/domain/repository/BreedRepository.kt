package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.ui.main.MainViewModel
import com.martafoderaro.smellycat.data.datasources.network.ResultWrapper

interface BreedRepository {
    suspend fun breeds(): ResultWrapper<List<Breed>>
    suspend fun breed(breedId: String): Breed
    suspend fun searchBreedImages(
        breedId: String,
        page: Int = 10,
        limit: Int = 100,
        order: String = MainViewModel.ImageOrderType.RANDOM.name
    ): ResultWrapper<List<BreedImage>>
}