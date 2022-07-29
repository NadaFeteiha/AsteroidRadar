package com.nadafeteiha.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.nadafeteiha.asteroidradar.repository.api.NASAApi
import com.nadafeteiha.asteroidradar.repository.api.asDatabaseModel
import com.nadafeteiha.asteroidradar.repository.api.asDomainModel
import com.nadafeteiha.asteroidradar.repository.database.AsteroidDatabase
import com.nadafeteiha.asteroidradar.domain.PictureOfDay
import com.nadafeteiha.asteroidradar.util.Constants
import com.nadafeteiha.asteroidradar.util.getNextSevenDaysFormattedDates
import com.nadafeteiha.asteroidradar.util.getPrevSevenDaysFormattedDates
import com.nadafeteiha.asteroidradar.util.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(
    private val database: AsteroidDatabase
) {

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(database.asteroidDao.getPictureOfDay()) {
            it?.asDomainModel()
        }

    /**
     * lastWeek because the api allow to get only 7 days
     * and I want to have history so I can show it to the user when select saved item (filter history)
     * */
    suspend fun refreshAsteroid() {
        lastWeek()
        nextWeek()
    }

    private suspend fun nextWeek() {
        withContext(Dispatchers.IO) {
            val nextWeek = getNextSevenDaysFormattedDates()
            val asteroids =
                parseAsteroidsJsonResult(
                    JSONObject(
                        NASAApi.retrofitService
                            .getNeoWsFeed(startDate = nextWeek.first(), endDate = nextWeek.last())
                    ), Constants.NEXT_WEEK
                )
            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        }
    }

    private suspend fun lastWeek() {
        withContext(Dispatchers.IO) {
            val prevWeek = getPrevSevenDaysFormattedDates()
            val asteroids =
                parseAsteroidsJsonResult(
                    JSONObject(
                        NASAApi.retrofitService
                            .getNeoWsFeed(startDate = prevWeek.first(), endDate = prevWeek.last())
                    ), Constants.LAST_WEEK
                )
            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
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