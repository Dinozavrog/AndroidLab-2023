package com.example.androidsecondsem.domain.location.useCase

import com.example.androidsecondsem.data.location.LocationDataSource
import com.example.androidsecondsem.domain.location.model.UserLocationModel

class GetLocationUseCase(
    private val locationDataSource: LocationDataSource
) {
    suspend operator fun invoke(): UserLocationModel = locationDataSource.getLocation()
}

