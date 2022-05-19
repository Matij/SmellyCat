package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository.BreedRepository
import com.martafoderaro.smellycat.data.datasources.network.ResultWrapper
import com.martafoderaro.smellycat.domain.usecase.CoroutineUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface IGetBreedsUseCase: CoroutineUseCase<ResultWrapper<List<Breed>>, Unit>

class GetBreedsUseCase @Inject constructor(
    private val breedRepository: BreedRepository
): IGetBreedsUseCase {
    override suspend fun invoke(parameters: Unit): Flow<ResultWrapper<List<Breed>>> {
        return breedRepository.breeds()
    }
}