package com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breed_images_table")
class BreedImageEntity(
    @PrimaryKey val id: String,
    val breedId: String? = null,
    val url: String,
)