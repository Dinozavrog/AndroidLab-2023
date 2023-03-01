package com.example.androidsecondsem.data.response

import com.example.androidsecondsem.BuildConfig
import com.example.androidsecondsem.data.WeatherApi
import com.example.androidsecondsem.data.interceptor.ApiKeyInterceptor
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = BuildConfig.API_ENDPOINT
const val API_KEY = BuildConfig.API_KEY
private const val QUERY_UNITS = "units"
private const val UNITS = "metric"

object Container {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
            }
        }

    private val unitsInterceptor = Interceptor { chain ->
        val original = chain.request()
        val newURL: HttpUrl = original.url.newBuilder()
            .addQueryParameter(QUERY_UNITS, UNITS)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(unitsInterceptor)
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

    suspend fun getWeather(cityName: String): WeatherResponse {
        return weatherApi.getWeatherByName(cityName)
    }

    suspend fun getWeather(cityId: Int): WeatherResponse {
        return weatherApi.getWeatherById(cityId)
    }

    suspend fun getCities(lat: Double, lon: Double, cnt: Int): List<City> {
        val cntString = cnt.toString()
        return weatherApi.getCities(lat, lon, cntString).list
    }
}
