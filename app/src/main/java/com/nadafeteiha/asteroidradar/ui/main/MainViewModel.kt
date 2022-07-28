package com.nadafeteiha.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.nadafeteiha.asteroidradar.repository.database.getDatabase
import com.nadafeteiha.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception

enum class ApiStatus { LOADING, ERROR, DONE }
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    val asteroidList = asteroidRepository.asteroid
    val pictureOfDay = asteroidRepository.pictureOfDay

    init {
        getAsteroidData()
        getPictureOfDay()
    }

    private fun getAsteroidData() {
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
            } catch (e: Exception) {

            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Error in Factory ViewModel")
        }
    }
}