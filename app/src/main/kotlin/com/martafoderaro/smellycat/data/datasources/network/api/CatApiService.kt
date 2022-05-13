package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.network.api

import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApiService {
    @JsonClass(generateAdapter = true)
    data class ApiBreed(
        val id: String,
        val name: String,
        val origin: String,
        val temperament: String,
        val wikipedia_url: String,
    )

    @JsonClass(generateAdapter = true)
    data class ApiBreedImage(
        val id: String,
        val url: String,
    )

    @GET("/breeds")
    suspend fun breeds(): List<ApiBreed>

    @GET("/images/search")
    suspend fun searchBreedImages(@Query("breed_ids") breedId: String): List<ApiBreedImage>
}