package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedImageEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage
import com.martafoderaro.smellycat.domain.mapper.Mapper
import javax.inject.Inject

class LocalBreedImageMapper @Inject constructor(): Mapper<BreedImage, BreedImageEntity> {
    override fun map(input: BreedImageEntity) = with(input) {
        BreedImage(id = id, url = url)
    }

    override fun map(input: List<BreedImageEntity>) = input.map { map(it) }
}