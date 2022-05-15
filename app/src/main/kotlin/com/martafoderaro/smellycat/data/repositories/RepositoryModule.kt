package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.repositories

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.repository.BreedRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun breedRepository(impl: BreedRepositoryImpl): BreedRepository
}