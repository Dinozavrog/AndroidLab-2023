package com.example.androidsecondsem.presentation.fragments.viewModel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy

class MainViewModel @AssistedInject constructor(
    @Assisted private val getWeatherByIdUseCase: GetWeatherByIdUseCase,
    @Assisted private val id: String
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo?>(null)
    val weatherInfo: LiveData<WeatherInfo?>
        get() = _weatherInfo

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?>
        get()  = _error

    var weatherDisposable: Disposable? = null

    fun loadWeather(){
        weatherDisposable = getWeatherByIdUseCase(id)
            .subscribeBy(onSuccess = { weatherInfo ->
                _weatherInfo.postValue(weatherInfo)
            }, onError = { error ->
                _error.postValue(error)
            })
    }

    @AssistedFactory
    interface MainViewModelFactory {
        fun create(getWeatherByIdUseCase: GetWeatherByIdUseCase, id: String?) :
                MainViewModel
    }

    override fun onCleared() {
        super.onCleared()
        weatherDisposable?.dispose()
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
