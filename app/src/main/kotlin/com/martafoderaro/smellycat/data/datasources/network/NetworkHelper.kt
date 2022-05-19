package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network

import com.martafoderaro.smellycat.core.CoroutineDispatchers
import com.martafoderaro.smellycat.data.datasources.network.ResultWrapper
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class NetworkHelper @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): ResultWrapper<T> {
        return withContext(dispatchers.io) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (e: IOException) {
                ResultWrapper.NetworkError(e.message)
            } catch (e: HttpException) {
                val code = e.code()
                ResultWrapper.GenericError(code, e)
            } catch (e: IllegalStateException) {
                ResultWrapper.GenericError()
            }
        }
    }


}