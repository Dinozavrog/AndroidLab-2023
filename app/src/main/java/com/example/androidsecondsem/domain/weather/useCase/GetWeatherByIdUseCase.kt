package com.example.androidsecondsem.domain.weather.useCase

import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single

class GetWeatherByIdUseCase(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(id: String): Single<WeatherInfo> {
        return weatherRepository.getWeatherById(id)
    }
}
