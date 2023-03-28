package com.example.androidsecondsem.data.location

import android.location.Location
import com.example.androidsecondsem.domain.location.model.UserLocationModel

fun Location.toLocationInfo(): UserLocationModel = UserLocationModel(
    latitude = latitude,
    longitude = longitude,
)
