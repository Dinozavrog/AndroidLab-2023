package com.example.androidsecondsem.di

import com.example.androidsecondsem.BuildConfig
import com.example.androidsecondsem.data.interceptor.ApiKeyInterceptor
import com.example.androidsecondsem.data.interceptor.UnitsInterceptor
import com.example.androidsecondsem.data.weather.response.WeatherApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @Named("loggining")
    fun provideLogginingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Named("api_key")
    fun provideApiKeyInterceptor() : Interceptor = ApiKeyInterceptor()

    @Provides
    @Named("units")
    fun  provideUnitsInterceptor(): Interceptor = UnitsInterceptor()

    @Provides
    fun provideHttpClient(
        @Named("api_key") apiKeyInterceptor: Interceptor,
        @Named("loggining") loggingInterceptor: Interceptor,
        @Named("units") unitsInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(unitsInterceptor)
            .connectTimeout(10L, TimeUnit.SECONDS)
            .build()

    @Provides
    fun provideRetrofit(
        httpClient: OkHttpClient,
        gsonFactory: GsonConverterFactory,
        @Named("base_url") baseUrl: String
    ): Retrofit = Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(gsonFactory)
            .baseUrl(baseUrl)
            .build()

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideWeatherApi(
        retrofit: Retrofit
    ): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @Named("base_url")
    fun provideBaseUrl(): String = BuildConfig.API_ENDPOINT

}
