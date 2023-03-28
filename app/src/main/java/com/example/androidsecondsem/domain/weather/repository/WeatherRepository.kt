package com.example.androidsecondsem.domain.weather.repository

import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.model.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherByName(name: String): WeatherInfo
    suspend fun getWeatherById(id: String): WeatherInfo
    suspend fun getCities(lat: Double?, lon: Double?): CitiesListInfo
}
