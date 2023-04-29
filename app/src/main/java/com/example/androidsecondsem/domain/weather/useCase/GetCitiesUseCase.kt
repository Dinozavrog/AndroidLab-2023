package com.example.androidsecondsem.domain.weather.useCase

import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single

class GetCitiesUseCase(
    private val weatherRepository: WeatherRepository
) {
     operator fun invoke(
        latitude: Double?,
        longitude: Double?,) : Single<CitiesListInfo> = weatherRepository.getCities(latitude, longitude)
}
