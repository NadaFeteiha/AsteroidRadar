package com.nadafeteiha.asteroidradar.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nadafeteiha.asteroidradar.domain.Asteroid
import com.nadafeteiha.asteroidradar.util.Constants

@Dao
interface AsteroidDao {
    @Insert
    suspend fun insert(asteroid: AsteroidEntity)

    @Update
    suspend fun update(asteroid: AsteroidEntity)

    @Query("DELETE FROM ${Constants.ASTEROID_TABLE_NAME}")
    suspend fun clear()

    @Query("SELECT * FROM  ${Constants.ASTEROID_TABLE_NAME}")
    fun getAllAsteroid(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM  ${Constants.ASTEROID_TABLE_NAME} WHERE closeApproachDate >= :today ORDER BY closeApproachDate DESC")
    fun getAllAsteroidTodayOnwards(today: String): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: AsteroidEntity)

    //******************** picture of the Day ********************\\

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = PictureOfDayEntity::class)
    suspend fun insert(pictureOfDayEntity: PictureOfDayEntity)

    @Update
    suspend fun update(pictureOfDayEntity: PictureOfDayEntity)

    @Query("SELECT * FROM  ${Constants.PICTURE_OF_DAY_TABLE} ORDER BY date DESC LIMIT 1")
    fun getPictureOfDay(): LiveData<PictureOfDayEntity>

}