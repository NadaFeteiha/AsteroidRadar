package com.nadafeteiha.asteroidradar.repository.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nadafeteiha.asteroidradar.domain.Asteroid
import com.nadafeteiha.asteroidradar.util.Constants

@Entity(tableName = Constants.ASTEROID_TABLE_NAME)
data class AsteroidEntity(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

@Entity(tableName = Constants.PICTURE_OF_DAY_TABLE)
data class PictureOfDayEntity(
    val date:String,
    @PrimaryKey
    val url: String,
    val mediaType: String,
    val title: String
)