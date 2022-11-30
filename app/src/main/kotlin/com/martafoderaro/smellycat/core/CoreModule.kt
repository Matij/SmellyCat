package com.martafoderaro.smellycat.com.martafoderaro.smellycat.core

import com.martafoderaro.smellycat.core.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {
    @Provides
    fun coroutineDispatchers() = CoroutineDispatcherProvider()
}