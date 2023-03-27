package com.example.androidsecondsem.domain.weather.model

import com.example.androidsecondsem.data.response.WeatherResponse

data class CitiesListInfo(
    val cod: String?,
    val count: Int?,
    val list: List<WeatherResponse?>?,
    val message: String?
)
