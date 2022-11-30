package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.repositories

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedDao
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedImageDao
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedImageEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network.NetworkHelper
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network.api.CatApiService
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper.ApiBreedImageMapper
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper.ApiBreedMapper
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper.LocalBreedImageMapper
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper.LocalBreedMapper
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository.BreedRepository
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.util.ConnectionStatusProvider
import com.martafoderaro.smellycat.data.datasources.network.ResultWrapper
import javax.inject.Inject

class BreedRepositoryImpl @Inject constructor(
    private val catApiService: CatApiService,
    private val breedDao: BreedDao,
    private val breedImageDao: BreedImageDao,
    private val apiBreedMapper: ApiBreedMapper,
    private val localBreedMapper: LocalBreedMapper,
    private val apiBreedImageMapper: ApiBreedImageMapper,
    private val localBreedImageMapper: LocalBreedImageMapper,
    private val connectionStatusProvider: ConnectionStatusProvider,
    private val networkHelper: NetworkHelper,
): BreedRepository {

    override suspend fun breeds() = when (connectionStatusProvider.isConnected()) {
        true -> remoteBreeds()
        else -> localBreeds()
    }

    override suspend fun breed(breedId: String) = localBreedMapper.map(breedDao.getBreed(breedId))

    private fun localBreeds(): ResultWrapper.Success<List<Breed>> {
        val localBreeds = breedDao.getAllBreeds()
        return ResultWrapper.Success(localBreedMapper.map(localBreeds))
    }

    private suspend fun remoteBreeds() = networkHelper.safeApiCall {
        val apiBreeds = catApiService.breeds()
        val localBreeds = apiBreedMapper.map(apiBreeds)
        storeBreedsInLocalMemory(localBreeds)
        localBreedMapper.map(localBreeds)
    }


    override suspend fun searchBreedImages(breedId: String, page: Int, limit: Int, order: String)
    = when (connectionStatusProvider.isConnected()) {
        true -> remoteBreedImages(breedId, page, limit, order)
        else -> localBreedImages(breedId)
    }

    private fun localBreedImages(breedId: String): ResultWrapper<List<BreedImage>> {
        val localBreedImages = breedImageDao.getBreedImages(breedId = breedId)
        return ResultWrapper.Success(localBreedImageMapper.map(localBreedImages))
    }

    private suspend fun remoteBreedImages(breedId: String, page: Int, limit: Int, order: String)
    = networkHelper.safeApiCall {
        val apiImages = catApiService.searchBreedImages(
                breedId = breedId, page = page, limit = limit, order = order
            )
            val localBreedImages = apiBreedImageMapper.map(apiImages)
            storeBreedImagesInLocalMemory(breedId, localBreedImages)
            localBreedImageMapper.map(localBreedImages)
        }

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