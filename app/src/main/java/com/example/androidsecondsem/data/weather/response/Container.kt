package com.example.androidsecondsem.data.weather.response

import android.content.Context
import com.example.androidsecondsem.BuildConfig
import com.example.androidsecondsem.domain.weather.useCase.GetCitiesUseCase
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByIdUseCase
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByNameUseCase
import com.example.androidsecondsem.data.weather.WeatherRepositoryImpl
import com.example.androidsecondsem.data.interceptor.ApiKeyInterceptor
import com.example.androidsecondsem.data.interceptor.UnitsInterceptor
import com.example.androidsecondsem.data.location.LocationDataSource
import com.example.androidsecondsem.domain.location.useCase.GetLocationUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = BuildConfig.API_ENDPOINT
const val API_KEY = BuildConfig.API_KEY

object Container {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
            }
        }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(UnitsInterceptor())
            .connectTimeout(10L, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    val weatherApi = retrofit.create(WeatherApi::class.java)

    val weatherRepository = WeatherRepositoryImpl(weatherApi)

    val weatherByIdUseCase: GetWeatherByIdUseCase
        get() = GetWeatherByIdUseCase(weatherRepository)

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    fun provideFusedLocation(
        applicationContext: Context
    ) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    val weatherByNameUseName: GetWeatherByNameUseCase
        get() = GetWeatherByNameUseCase(weatherRepository)

    val getCities: GetCitiesUseCase
        get() = GetCitiesUseCase(weatherRepository)

    private val locationDataSource: LocationDataSource
        get() = LocationDataSource(fusedLocationProviderClient ?: throw NullPointerException("fusedLocation is null"))

    val locationUseCase: GetLocationUseCase
        get() = GetLocationUseCase(locationDataSource)

}
