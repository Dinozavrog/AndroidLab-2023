package com.example.androidsecondsem.domain.weather.repository

import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {
    fun getWeatherByName(name: String): Single<WeatherInfo>
    fun getWeatherById(id: String): Single<WeatherInfo>
    fun getCities(lat: Double?, lon: Double?): Single<CitiesListInfo>
}
