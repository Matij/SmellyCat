package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.domain.mapper.Mapper
import javax.inject.Inject

class LocalBreedMapper @Inject constructor(): Mapper<Breed, BreedEntity> {
    override fun map(input: BreedEntity) = with(input) {
        Breed(
            id = breedId,
            name = name,
            origin = origin,
            temperament = temperament,
            wikiUrl = wikiUrl
        )
    }

    override fun map(input: List<BreedEntity>) = input.map { map(it) }
}