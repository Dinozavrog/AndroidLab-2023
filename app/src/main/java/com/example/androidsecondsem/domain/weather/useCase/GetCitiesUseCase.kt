package com.example.androidsecondsem.domain.weather.useCase

import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository

class GetCitiesUseCase(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(
        latitude: Double?,
        longitude: Double?,) : CitiesListInfo = weatherRepository.getCities(latitude, longitude)
}
