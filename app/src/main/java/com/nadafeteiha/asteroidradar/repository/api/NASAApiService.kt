package com.nadafeteiha.asteroidradar.repository.api

import com.nadafeteiha.asteroidradar.domain.PictureOfDay
import com.nadafeteiha.asteroidradar.util.Constants
import com.nadafeteiha.asteroidradar.util.Constants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface NASAApiService {
    @GET("planetary/apod?")
    suspend fun getProperties(
        @Query("api_key") api: String = Constants.API_KEY
    ): PictureOfDay

    @GET("neo/rest/v1/feed?")
    suspend fun getNeoWsFeed(
        @Query("api_key") api: String = Constants.API_KEY,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): String
}

object NASAApi {
    val retrofitService: NASAApiService by lazy { retrofit.create(NASAApiService::class.java) }
}