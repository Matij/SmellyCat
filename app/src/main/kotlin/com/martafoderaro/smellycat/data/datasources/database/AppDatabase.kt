package com.martafoderaro.smellycat.data.datasources.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedDao
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedEntity
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedImageDao
import com.martafoderaro.smellycat.com.martafoderaro.smellycat.data.datasources.database.BreedImageEntity

@Database(entities = [BreedEntity::class, BreedImageEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "cats_app_database")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
    abstract fun breedDao(): BreedDao
    abstract fun breedImageDao(): BreedImageDao
}