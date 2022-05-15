package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BreedDao {
    @Query("SELECT * FROM breed_table")
    fun getAllBreeds(): List<BreedEntity>

    @Query("SELECT * FROM breed_table WHERE breedId == :breedId")
    fun getBreed(breedId: String): BreedEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllBreeds(breeds: List<BreedEntity>)

    @Query("DELETE FROM breed_table")
    suspend fun deleteAllBreeds()
}