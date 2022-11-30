package com.martafoderaro.smellycat.di

import android.app.Application
import android.content.Context
import com.martafoderaro.smellycat.BuildConfig
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.util.ConnectionStatusProvider
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
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    fun provideConnectionStatusProvider() = ConnectionStatusProvider()
}