package com.example.androidsecondsem.domain.location.useCase

import com.example.androidsecondsem.data.location.LocationDataSource
import com.example.androidsecondsem.domain.location.model.UserLocationModel
import io.reactivex.rxjava3.core.Flowable

class GetLocationUseCase(
    private val locationDataSource: LocationDataSource
) {
    operator fun invoke(): Flowable<UserLocationModel> = locationDataSource.getLocation()
}

