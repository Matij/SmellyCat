package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedImageEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network.api.CatApiService
import com.martafoderaro.smellycat.domain.mapper.Mapper
import javax.inject.Inject

class ApiBreedImageMapper @Inject constructor(): Mapper<BreedImageEntity, CatApiService.ApiBreedImage> {
    override fun map(input: CatApiService.ApiBreedImage) = with(input) {
        BreedImageEntity(id = id, url = url)
    }

    override fun map(input: List<CatApiService.ApiBreedImage>) = input.map { map(it) }
}