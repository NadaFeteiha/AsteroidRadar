package com.nadafeteiha.asteroidradar.repository.api

import com.nadafeteiha.asteroidradar.repository.database.AsteroidEntity
import com.nadafeteiha.asteroidradar.repository.database.PictureOfDayEntity
import com.nadafeteiha.asteroidradar.domain.Asteroid
import com.nadafeteiha.asteroidradar.domain.PictureOfDay
import com.nadafeteiha.asteroidradar.util.convertDateToLong
import com.nadafeteiha.asteroidradar.util.convertLongToDate
import com.squareup.moshi.JsonClass

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun List<Asteroid>.asDatabaseModel(): Array<AsteroidEntity> {
    return this.map {
        AsteroidEntity(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

//******************* picture of the Day*******************\\
fun PictureOfDayEntity.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url,
        date = this.date/*.convertLongToDate()*/
    )
}

fun PictureOfDay.asDatabaseModel(): PictureOfDayEntity {
    return PictureOfDayEntity(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url,
        date = this.date/*.convertDateToLong()*/

    )
}
