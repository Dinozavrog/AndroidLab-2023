package com.example.androidsecondsem.data.location

import android.annotation.SuppressLint
import com.example.androidsecondsem.domain.location.model.UserLocationModel
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LocationDataSource(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    @SuppressLint("MissingPermission")
    suspend fun getLocation(): UserLocationModel = withContext(dispatcher) {
        fusedLocationProviderClient.lastLocation.await().let {
            it?.toLocationInfo() ?: UserLocationModel(
                latitude = 54.5299,
                longitude = 52.8039,
            )
        }
    }

}
