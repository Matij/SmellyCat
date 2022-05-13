package com.martafoderaro.smellycat.com.martafoderaro.smellycat.core

import com.martafoderaro.smellycat.core.CoroutineDispatchers
import com.martafoderaro.smellycat.core.DefaultCoroutineDispatchers
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CoreModule {
    @Binds
    @Singleton
    abstract fun coroutineDispatchers(impl: DefaultCoroutineDispatchers): CoroutineDispatchers
}