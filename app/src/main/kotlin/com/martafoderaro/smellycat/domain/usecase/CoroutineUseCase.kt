package com.martafoderaro.smellycat.domain.usecase

import kotlinx.coroutines.flow.Flow

interface CoroutineUseCase<out Output, in Parameters> {
    suspend fun invoke(parameters: Parameters): Flow<Output>
}