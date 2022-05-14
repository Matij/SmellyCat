package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.repositories

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network.api.CatApiService
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository.BreedRepository
import com.martafoderaro.smellycat.core.CoroutineDispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BreedRepositoryImpl @Inject constructor(
    private val catApiService: CatApiService,
    private val dispatchers: CoroutineDispatchers,
): BreedRepository {

    override fun breeds() = flow {
        val apiBreeds = catApiService.breeds()

        emit(apiBreeds.map { apiBreed ->
            with(apiBreed) {
                Breed(
                    id = id,
                    name = name,
                    origin = origin,
                    temperament = temperament,
                    wikiUrl = wikipedia_url,
                )
            }
        })
    }.flowOn(dispatchers.io)

    override fun searchBreedImages(breedId: String, page: Int, limit: Int, order: String) = flow {
        val apiImages = catApiService.searchBreedImages(
            breedId = breedId, page = page, limit = limit, order = order
        )
        emit(apiImages.map { apiImage ->
            with(apiImage) { BreedImage(id = id, url = url) }
        })
    }.flowOn(dispatchers.io)
}