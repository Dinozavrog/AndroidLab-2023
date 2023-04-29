package com.example.androidsecondsem.data.weather.response

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("find?")
    fun getCities(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("cnt") cnt: Int? = 10
    ): Single<CitiesResponse>

    @GET("weather?")
    fun getWeatherByName(@Query("q") city: String): Single<WeatherResponse>

    @GET("weather?")
    fun getWeatherById(@Query("id") id: String): Single<WeatherResponse>
}
