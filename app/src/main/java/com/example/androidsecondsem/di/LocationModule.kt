package com.example.androidsecondsem.di

import android.content.Context
import com.example.androidsecondsem.data.location.LocationDataSource
import com.example.androidsecondsem.domain.location.useCase.GetLocationUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {

    @Provides
    fun provideFusedLocationClient(
        context: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun provideLocationDataSource(
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LocationDataSource = LocationDataSource(fusedLocationProviderClient)

    @Provides
    fun provideLocationUseCase(
        locationDataSource: LocationDataSource
    ):GetLocationUseCase = GetLocationUseCase(locationDataSource)

}
