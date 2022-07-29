package com.nadafeteiha.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.nadafeteiha.asteroidradar.domain.Asteroid
import com.nadafeteiha.asteroidradar.repository.database.getDatabase
import com.nadafeteiha.asteroidradar.repository.AsteroidRepository
import com.nadafeteiha.asteroidradar.repository.api.asDomainModel
import com.nadafeteiha.asteroidradar.util.getNextSevenDaysFormattedDates
import com.nadafeteiha.asteroidradar.util.getToday
import kotlinx.coroutines.launch
import java.lang.Exception

enum class ApiStatus { LOADING, ERROR, DONE }
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status
    private var _showEvent = MutableLiveData<Boolean?>()
    val showEvent: LiveData<Boolean?>
        get() = _showEvent

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidsList: LiveData<List<Asteroid>>
        get() = _asteroidList

    val pictureOfDay = asteroidRepository.pictureOfDay

    init {
        showAllAsteroid()
        updateAsteroidData()
        getPictureOfDay()
    }

    private fun updateAsteroidData() {
        setShowEvent()
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroid()
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                asteroidRepository.refreshPictureOfTheDay()
            } catch (_: Exception) {

            }
        }
    }

    fun showNextWeekAsteroid() {
        viewModelScope.launch {
            val week = getNextSevenDaysFormattedDates()
            database.asteroidDao.getAllAsteroidTodayOnwards(week.first())
                .collect { asteroids ->
                    _asteroidList.value = asteroids.asDomainModel()
                }
        }
    }

    private fun showAllAsteroid() {
        viewModelScope.launch {
            database.asteroidDao.getAllAsteroidTodayOnwards(getToday())
                .collect { asteroids ->
                    _asteroidList.value = asteroids.asDomainModel()
                }
        }
    }

    fun showSavedAsteroid() {
        viewModelScope.launch {
            database.asteroidDao.getSavedBeforeTodayAsteroid(getToday())
                .collect { asteroids ->
                    _asteroidList.value = asteroids.asDomainModel()
                }
        }
    }

    fun showTodayAsteroid() {
        viewModelScope.launch {
            database.asteroidDao.getTodayAsteroid(getToday())
                .collect { asteroids ->
                    _asteroidList.value = asteroids.asDomainModel()
                }
        }
    }

    fun doneShowEvent() {
        _showEvent.value = null
    }

    fun setShowEvent() {
        _showEvent.value = true

    }

}