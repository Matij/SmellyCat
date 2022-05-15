package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.repositories

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedDao
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedImageDao
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedImageEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network.api.CatApiService
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper.ApiBreedImageMapper
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper.ApiBreedMapper
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper.LocalBreedImageMapper
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper.LocalBreedMapper
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository.BreedRepository
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.util.isConnected
import com.martafoderaro.smellycat.core.CoroutineDispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class BreedRepositoryImpl @Inject constructor(
    private val catApiService: CatApiService,
    private val breedDao: BreedDao,
    private val breedImageDao: BreedImageDao,
    private val apiBreedMapper: ApiBreedMapper,
    private val localBreedMapper: LocalBreedMapper,
    private val apiBreedImageMapper: ApiBreedImageMapper,
    private val localBreedImageMapper: LocalBreedImageMapper,
    private val dispatchers: CoroutineDispatchers,
): BreedRepository {

    override fun breeds() = when (isConnected()) {
        true -> remoteBreeds()
        else -> localBreeds()
    }

    override fun breed(breedId: String) = flow {
        emit(localBreedMapper.map(breedDao.getBreed(breedId)))
    }.flowOn(dispatchers.io)

    private fun localBreeds() = flow {
        val localBreeds = breedDao.getAllBreeds()
        emit(localBreedMapper.map(localBreeds))
    }.flowOn(dispatchers.io)

    private fun remoteBreeds() = flow {
        val apiBreeds = catApiService.breeds()
        val localBreeds = apiBreedMapper.map(apiBreeds)
        storeBreedsInLocalMemory(localBreeds)
        emit(localBreedMapper.map(localBreeds))
    }.flowOn(dispatchers.io)

    override fun searchBreedImages(breedId: String, page: Int, limit: Int, order: String) = when (isConnected()) {
        true -> remoteBreedImages(breedId, page, limit, order)
        else -> localBreedImages(breedId)
    }

    private fun localBreedImages(breedId: String) = flow {
        val localBreedImages = breedImageDao.getBreedImages(breedId = breedId)
        emit(localBreedImageMapper.map(localBreedImages))
    }.flowOn(dispatchers.io)

    private fun remoteBreedImages(breedId: String, page: Int, limit: Int, order: String) = flow {
        val apiImages = catApiService.searchBreedImages(
            breedId = breedId, page = page, limit = limit, order = order
        )
        val localBreedImages = apiBreedImageMapper.map(apiImages)
        storeBreedImagesInLocalMemory(breedId, localBreedImages)
        emit(localBreedImageMapper.map(localBreedImages))
    }.flowOn(dispatchers.io)

    private suspend fun storeBreedsInLocalMemory(data: List<BreedEntity>) {
        breedDao.insertAllBreeds(data)
    }
    private suspend fun storeBreedImagesInLocalMemory(breedId: String, data: List<BreedImageEntity>) {
        //TODO Need data analysis to assess whether an image can be associated to multiple breeds.
        // If this is the case, persistence layer tables will have one-to-many relationship.
        val manipulatedData = data.map { with (it) {
            BreedImageEntity(
                breedId = breedId,
                id = id,
                url = url
            )
        } }
        breedImageDao.insertAllBreedImages(manipulatedData)
    }

}