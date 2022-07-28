package com.nadafeteiha.asteroidradar.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nadafeteiha.asteroidradar.util.Constants


private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                Constants.ASTEROID_DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}

@Database(
    entities = [AsteroidEntity::class, PictureOfDayEntity::class],
    version = 12,
    exportSchema = false
)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}