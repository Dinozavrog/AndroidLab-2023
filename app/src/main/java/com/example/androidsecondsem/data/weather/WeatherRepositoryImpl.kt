package com.example.androidsecondsem.data.weather

import com.example.androidsecondsem.data.weather.mappers.toCitiesInfo
import com.example.androidsecondsem.data.weather.mappers.toWeatherInfo
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.data.weather.response.WeatherApi
import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherRepositoryImpl(
    private val api: WeatherApi
): WeatherRepository {

    override fun getWeatherByName(name: String): Single<WeatherInfo> =
        api.getWeatherByName(name)
            .map {
                it.toWeatherInfo()
            }
            .subscribeOn(Schedulers.io())

    override fun getWeatherById(id: String): Single<WeatherInfo> =
        api.getWeatherById(id)
            .map {
                it.toWeatherInfo()
            }
            .subscribeOn(Schedulers.io())

    override fun getCities(
        lat: Double?,
        lon: Double?,
    ): Single<CitiesListInfo> =
        api.getCities(lat, lon, 10)
            .map {
                it.toCitiesInfo()
            }
            .subscribeOn(Schedulers.io())
}
