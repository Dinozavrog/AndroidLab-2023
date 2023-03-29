package com.example.androidsecondsem.presentation.fragments.viewModel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByIdUseCase

class MainViewModel(
    private val getWeatherByIdUseCase: GetWeatherByIdUseCase,
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo?>(null)
    val weatherInfo: LiveData<WeatherInfo?>
        get() = _weatherInfo

    suspend fun loadWeather(id: String){
        _weatherInfo.value = getWeatherByIdUseCase(id)
    }

    companion object {
        fun provideFactory(
            getWeatherByIdUseCase: GetWeatherByIdUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(getWeatherByIdUseCase)
            }
        }
    }
}
