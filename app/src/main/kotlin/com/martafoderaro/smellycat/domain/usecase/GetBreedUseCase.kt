package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository.BreedRepository
import com.martafoderaro.smellycat.domain.usecase.CoroutineUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class GetBreedParameters(
    val breedId: String
)

interface IGetBreedUseCase: CoroutineUseCase<Breed, GetBreedParameters>

class GetBreedUseCase @Inject constructor(
    private val breedRepository: BreedRepository
): IGetBreedUseCase {
    override suspend fun invoke(parameters: GetBreedParameters): Flow<Breed> {
        return breedRepository.breed(breedId = parameters.breedId)
    }
}
