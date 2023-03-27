package com.example.androidsecondsem.presentation.fragments.viewModel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.androidsecondsem.data.response.Container
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
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val getWeatherByIdUseCase = Container.weatherByIdUseCase
                return MainViewModel(
                    getWeatherByIdUseCase,
                    ) as T
            }
        }
    }
}
