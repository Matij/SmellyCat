package com.martafoderaro.smellycat.di

import com.martafoderaro.smellycat.BuildConfig
import com.martafoderaro.smellycat.data.datasources.network.RestDebug
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @RestDebug
    fun provideRestDebug(): Boolean = BuildConfig.BUILD_TYPE == "debug"
}