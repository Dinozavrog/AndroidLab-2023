package com.example.androidsecondsem.data

import com.example.androidsecondsem.data.response.API_KEY
import com.example.androidsecondsem.data.response.CityList
import com.example.androidsecondsem.data.response.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
        @Query("appid") key: String = API_KEY
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherById(
        @Query("id") id: Int,
        @Query("units") units: String = "metric",
        @Query("appid") key: String = API_KEY
    ): WeatherResponse

    @GET("find?")
    suspend fun getCities(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: String
    ): CityList

    @GET("weather?")
    suspend fun getWeatherByName(@Query("q") city: String): WeatherResponse

    @GET("weather?")
    suspend fun getWeatherById(@Query("id") id: Int): WeatherResponse
}
