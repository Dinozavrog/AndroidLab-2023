package com.example.androidsecondsem.data.location

import android.annotation.SuppressLint
import com.example.androidsecondsem.domain.location.model.UserLocationModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await

class LocationDataSource(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    @SuppressLint("MissingPermission")
    suspend fun getLocation(): UserLocationModel = fusedLocationProviderClient.lastLocation.await().let {
        it?.toLocationInfo() ?: UserLocationModel(
            latitude = 54.5299,
            longitude = 52.8039,
        )
    }
}
