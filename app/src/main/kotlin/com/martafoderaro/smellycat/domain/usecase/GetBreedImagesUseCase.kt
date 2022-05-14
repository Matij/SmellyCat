package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository.BreedRepository
import com.martafoderaro.smellycat.domain.usecase.CoroutineUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class GetBreedImagesParameters(
    val breedId: String,
    val page: Int,
    val limit: Int,
    val order: String
)

interface IGetBreedImagesCase: CoroutineUseCase<List<BreedImage>, GetBreedImagesParameters>

class GetBreedImagesUseCase @Inject constructor(
    private val breedRepository: BreedRepository
): IGetBreedImagesCase {
    override suspend fun invoke(parameters: GetBreedImagesParameters): Flow<List<BreedImage>> {
        return breedRepository.searchBreedImages(
                breedId = parameters.breedId,
                page = parameters.page,
                limit = parameters.limit,
                order = parameters.order
            )
    }

}