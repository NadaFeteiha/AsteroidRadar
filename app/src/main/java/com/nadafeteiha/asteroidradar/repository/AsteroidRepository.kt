package com.nadafeteiha.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.nadafeteiha.asteroidradar.repository.api.NASAApi
import com.nadafeteiha.asteroidradar.repository.api.asDatabaseModel
import com.nadafeteiha.asteroidradar.repository.api.asDomainModel
import com.nadafeteiha.asteroidradar.repository.database.AsteroidDatabase
import com.nadafeteiha.asteroidradar.domain.Asteroid
import com.nadafeteiha.asteroidradar.domain.PictureOfDay
import com.nadafeteiha.asteroidradar.util.getNextSevenDaysFormattedDates
import com.nadafeteiha.asteroidradar.util.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(
    private val database: AsteroidDatabase
) {
    val asteroid: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao.getAllAsteroidTodayOnwards(
                getNextSevenDaysFormattedDates().first()
            )
        )
        {
            it.asDomainModel()
        }

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(database.asteroidDao.getPictureOfDay()) {
            it?.asDomainModel()
        }

    suspend fun refreshAsteroid() {
        withContext(Dispatchers.IO) {
            val days = getNextSevenDaysFormattedDates()
            val asteroid =
                parseAsteroidsJsonResult(
                    JSONObject(
                        NASAApi.retrofitService
                            .getNeoWsFeed(startDate = days.first(), endDate = days.last())
                    )
                )
            database.asteroidDao.insertAll(*asteroid.asDatabaseModel())
        }
    }

    suspend fun clearAsteroid() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.clear()
        }
    }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            val pictureOfDay = NASAApi.retrofitService.getProperties()
            database.asteroidDao.insert(pictureOfDay.asDatabaseModel())
        }
    }
}