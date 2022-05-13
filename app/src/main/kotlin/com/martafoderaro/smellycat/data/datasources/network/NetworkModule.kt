package com.martafoderaro.smellycat.data.datasources.network

import com.martafoderaro.smellycat.BuildConfig
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network.api.CatApiService
import com.martafoderaro.smellycat.data.datasources.network.interceptor.AuthApiKeyInterceptor
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class RestApiBaseUrl

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class RestApiKey

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class RestDebug

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class OkHttpInterceptor

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
@MustBeDocumented
annotation class OkHttpNetworkInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorOkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @RestApiBaseUrl
    fun provideBuildConfigRestCatApiBaseUrl(): String = BuildConfig.REST_CAT_API_BASE_URL

    @Provides
    @RestApiKey
    fun provideBuildConfigRestCatApiKey(): String = BuildConfig.REST_CAT_API_KEY

    @Provides
    fun provideOkHttpClient(
        @OkHttpInterceptor interceptors: Set<@JvmSuppressWildcards Interceptor>,
        @OkHttpNetworkInterceptor networkInterceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .apply {
            interceptors
                .forEach {
                    addInterceptor(it)
                }
            networkInterceptors
                .forEach {
                    addNetworkInterceptor(it)
                }
        }
        .build()

    @Provides
    @IntoSet
    @OkHttpNetworkInterceptor
    fun provideHttpLoggingInterceptor(@RestDebug debug: Boolean): Interceptor =
        HttpLoggingInterceptor().apply {
            level =
                if (debug) HttpLoggingInterceptor.Level.BODY
                else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    fun provideRetrofit(
        @RestApiBaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()

    @Provides
    @AuthInterceptorOkHttpClient
    fun provideAuthInterceptorOkHttpClient(
        authInterceptor: AuthApiKeyInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class BindAuthApiKeyInterceptor {
        @Binds
        @IntoSet
        @OkHttpInterceptor
        abstract fun bindAuthInterceptor(bind: AuthApiKeyInterceptor): Interceptor
    }

    // Api

    @Provides
    fun providesCatApiService(retrofit: Retrofit): CatApiService =
        retrofit.create(CatApiService::class.java)
}