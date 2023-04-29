package com.example.androidsecondsem.domain.weather.model

data class WeatherInfo(
    val id: Int,
    val name: String,
    val icon: String,
    val weatherDescription: String,
    val temperature: Double,
    val wind: Int,
    val clouds: String
)
