package com.example.androidsecondsem.data.weather

import com.example.androidsecondsem.data.weather.response.CitiesResponse
import com.example.androidsecondsem.domain.weather.model.CitiesListInfo

fun CitiesResponse.toCitiesInfo(): CitiesListInfo = CitiesListInfo(
    cod = cod,
    count = count,
    list = list,
    message = message,
)
