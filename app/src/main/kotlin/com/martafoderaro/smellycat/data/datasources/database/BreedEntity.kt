package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breed_table")
class BreedEntity(
    @PrimaryKey val breedId: String,
    val name: String,
    val origin: String,
    val description: String,
    val temperament: String,
    val wikiUrl: String?,
)