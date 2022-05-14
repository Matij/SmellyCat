package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.usecase

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {
    @Binds
    @ViewModelScoped
    fun bindGetBreedsUseCase(useCase: GetBreedsUseCase): IGetBreedsUseCase
    @Binds
    @ViewModelScoped
    fun bindGetBreedImagesUseCase(useCase: GetBreedImagesUseCase): IGetBreedImagesCase
}