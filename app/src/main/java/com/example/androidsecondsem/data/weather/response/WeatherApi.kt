package com.example.androidsecondsem.data.weather.response

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("find?")
    suspend fun getCities(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("cnt") cnt: Int? = 10
    ): CitiesResponse

    @GET("weather?")
    suspend fun getWeatherByName(@Query("q") city: String): WeatherResponse

    @GET("weather?")
    suspend fun getWeatherById(@Query("id") id: String): WeatherResponse
}
