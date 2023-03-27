package com.example.androidsecondsem.data.weather

import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.data.response.City
import com.example.androidsecondsem.data.response.WeatherApi
import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getWeatherByName(name: String): WeatherInfo {
        val response = api.getWeatherByName(name)
        return WeatherInfo(
            id = response.id,
            name = response.name,
            icon = response.weather[0].icon,
            weatherDescription = response.weather[0].description,
            temperature = response.main.temp,
            wind = response.wind.deg,
            clouds = response.clouds.all.toString() + "%"
        )
    }

    override suspend fun getWeatherById(id: String): WeatherInfo {
        val response = api.getWeatherById(id)
        return WeatherInfo(
            id = response.id,
            name = response.name,
            icon = response.weather[0].icon,
            weatherDescription = response.weather[0].description,
            temperature = response.main.temp,
            wind = response.wind.deg,
            clouds = response.clouds.all.toString() + "%"
        )
    }

    override suspend fun getCities(
        lat: Double?,
        lon: Double?,
    ): CitiesListInfo =  api.getCities(lat, lon, 10).toCitiesInfo()

    private fun convertCity(city: City): WeatherInfo {
        return WeatherInfo(
            id = city.id,
            name = city.name,
            icon = city.weather[0].icon,
            weatherDescription = city.weather[0].description,
            temperature = city.main.temp,
            wind = city.wind.deg,
            clouds = city.clouds.toString() + "%"
        )
    }
}
