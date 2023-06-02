package com.example.androidsecondsem.domain.weather.useCase

import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCitiesUseCase(
    private val weatherRepository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(
        latitude: Double?,
        longitude: Double?,) : CitiesListInfo {
        return withContext(dispatcher) {
            weatherRepository.getCities(latitude, longitude)
        }
    }
}
