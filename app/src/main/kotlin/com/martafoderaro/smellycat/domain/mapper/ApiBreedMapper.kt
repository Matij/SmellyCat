package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network.api.CatApiService
import com.martafoderaro.smellycat.domain.mapper.Mapper
import javax.inject.Inject

class ApiBreedMapper @Inject constructor(): Mapper<BreedEntity, CatApiService.ApiBreed> {
    override fun map(input: CatApiService.ApiBreed) = with(input) {
        BreedEntity(
            breedId = id,
            name = name,
            origin = origin,
            temperament = temperament,
            wikiUrl = wikipedia_url,
        )
    }

    override fun map(input: List<CatApiService.ApiBreed>) = input.map { map(it) }
}