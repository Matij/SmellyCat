package com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.mapper

import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedImageEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network.api.CatApiService
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.BreedImage
import com.martafoderaro.smellycat.domain.mapper.Mapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface MapperModule {
    @Binds
    fun bindApiBreedMapper(mapper: ApiBreedMapper): Mapper<BreedEntity, CatApiService.ApiBreed>
    @Binds
    fun bindLocalBreedMapper(mapper: LocalBreedMapper): Mapper<Breed, BreedEntity>
    @Binds
    fun bindApiBreedImageMapper(mapper: ApiBreedImageMapper): Mapper<BreedImageEntity, CatApiService.ApiBreedImage>
    @Binds
    fun bindLocalBreedImageMapper(mapper: LocalBreedImageMapper): Mapper<BreedImage, BreedImageEntity>
}