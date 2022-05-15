package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database

import android.content.Context
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.domain.model.Breed
import com.martafoderaro.smellycat.data.datasources.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase = AppDatabase.getDatabase(context)
    @Provides
    fun provideBreedDao(appDatabase: AppDatabase): BreedDao = appDatabase.breedDao()
    @Provides
    fun provideBreedImageDao(appDatabase: AppDatabase): BreedImageDao = appDatabase.breedImageDao()
}