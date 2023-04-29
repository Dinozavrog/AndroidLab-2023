package com.example.androidsecondsem.data.weather.mappers

import com.example.androidsecondsem.data.weather.response.WeatherResponse
import com.example.androidsecondsem.domain.weather.model.WeatherInfo

fun WeatherResponse.toWeatherInfo(): WeatherInfo = WeatherInfo(
    id = id,
    name = name,
    icon = weather[0].icon,
    weatherDescription = weather[0].description,
    temperature = main.temp,
    wind = wind.deg,
    clouds = clouds.all.toString() + "%"
)
