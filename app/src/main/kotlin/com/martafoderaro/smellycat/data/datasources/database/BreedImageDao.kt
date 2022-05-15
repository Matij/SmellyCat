package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BreedImageDao {
    @Query("SELECT * FROM breed_images_table")
    fun getAllBreedImages(): List<BreedImageEntity>

    @Query("SELECT * FROM breed_images_table WHERE breedId == :breedId")
    fun getBreedImages(breedId: String): List<BreedImageEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllBreedImages(images: List<BreedImageEntity>)

    @Query("DELETE FROM breed_images_table")
    suspend fun deleteAllBreedImages()
}