package com.example.androidsecondsem.di

import com.example.androidsecondsem.data.weather.WeatherRepositoryImpl
import com.example.androidsecondsem.data.weather.response.WeatherApi
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository
import com.example.androidsecondsem.domain.weather.useCase.GetCitiesUseCase
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByIdUseCase
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WeatherModule {

    @Provides
    fun provideWeatherRepository(
        weatherApi: WeatherApi
    ): WeatherRepository = WeatherRepositoryImpl(weatherApi)

    @Provides
    fun provideWeatherByIdUseCase(
        repository: WeatherRepository
    ): GetWeatherByIdUseCase = GetWeatherByIdUseCase(repository)

    @Provides
    fun provideWeatherByNameUseName(
        repository: WeatherRepository
    ): GetWeatherByNameUseCase = GetWeatherByNameUseCase(repository)

    @Provides
    fun getCities(
        repository: WeatherRepository
    ): GetCitiesUseCase = GetCitiesUseCase(repository)
}
