package com.example.androidsecondsem.domain.weather.useCase

import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single

class GetWeatherByNameUseCase(
    private val weatherRepository: WeatherRepository,
) {
    operator fun invoke(name: String): Single<WeatherInfo> {
        return weatherRepository.getWeatherByName(name)
    }
}
