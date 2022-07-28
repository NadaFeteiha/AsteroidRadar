package com.nadafeteiha.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nadafeteiha.asteroidradar.repository.database.getDatabase
import com.nadafeteiha.asteroidradar.repository.AsteroidRepository
import com.nadafeteiha.asteroidradar.util.Constants
import retrofit2.HttpException

class RefreshDataWorker (appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = Constants.REFRESH_DATA_WORKER
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.clearAsteroid()
            repository.refreshAsteroid()
            repository.refreshPictureOfTheDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }    }
}