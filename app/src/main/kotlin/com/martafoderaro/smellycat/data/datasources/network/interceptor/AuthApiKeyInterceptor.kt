package com.martafoderaro.smellycat.data.datasources.network.interceptor

import com.martafoderaro.smellycat.data.datasources.network.RestApiKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthApiKeyInterceptor @Inject constructor(
    @RestApiKey private val apiKey: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder().apply {
            addHeader("x-api-key", apiKey)
        }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}