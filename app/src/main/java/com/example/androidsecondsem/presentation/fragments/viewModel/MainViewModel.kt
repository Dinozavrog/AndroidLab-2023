package com.example.androidsecondsem.presentation.fragments.viewModel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class MainViewModel @AssistedInject constructor(
    @Assisted private val getWeatherByIdUseCase: GetWeatherByIdUseCase,
    @Assisted private val id: String
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo?>(null)
    val weatherInfo: LiveData<WeatherInfo?>
        get() = _weatherInfo

    suspend fun loadWeather(){
        _weatherInfo.value = getWeatherByIdUseCase(id)
    }

    @AssistedFactory
    interface MainViewModelFactory {
        fun create(getWeatherByIdUseCase: GetWeatherByIdUseCase, id: String?) :
                MainViewModel
    }

    companion object {
        fun provideFactory(
            getWeatherByIdUseCase: GetWeatherByIdUseCase,
            assistedFactory: MainViewModelFactory,
            id: String?
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                assistedFactory.create(getWeatherByIdUseCase, id)
            }
        }
    }
}
