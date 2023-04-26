package com.example.androidsecondsem.presentation.fragments.viewModel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.androidsecondsem.data.weather.response.WeatherResponse
import com.example.androidsecondsem.domain.location.model.UserLocationModel
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.useCase.GetCitiesUseCase
import com.example.androidsecondsem.domain.location.useCase.GetLocationUseCase
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val getWeatherByNameUseCase: GetWeatherByNameUseCase
) : ViewModel() {

    private val _weatherInfo = MutableLiveData<WeatherInfo?>(null)
    val weatherInfo: LiveData<WeatherInfo?>
        get()  = _weatherInfo

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?>
        get()  = _error

    private val _location = MutableLiveData<UserLocationModel?>()
    val location: LiveData<UserLocationModel?>
        get() = _location

    private val _citiesList = MutableLiveData<List<WeatherResponse?>?>()
    val citiesList: LiveData<List<WeatherResponse?>?>
        get() = _citiesList

    private val _navigation = SingleLiveEvent<Int?>()
    val navigation: SingleLiveEvent<Int?>
        get() = _navigation

    fun onWeatherClick(weatherResponse: WeatherResponse) {
        val cityId: Int = weatherResponse.id
        if (cityId != null) {
            _navigation.value = cityId
        }
    }

    fun loadWeather(name: String) {
        viewModelScope.launch {
            try {
                if (!getWeatherByNameUseCase(name).id.toString().isNullOrEmpty())
                    _navigation.value = getWeatherByNameUseCase(name).id
            }
            catch (error: Throwable) {
                _error.value = error
            }
        }
    }

    suspend fun locationPerm(res: Boolean) {
        if (res) {
            getLocation()
        } else {
            _location.value = UserLocationModel(
                latitude = 54.5299,
                longitude = 52.8039,
            )
        }
    }

    private suspend fun getLocation() {
        _location.value = getLocationUseCase.invoke()
    }

    suspend fun getCities(latitude: Double?, longitude: Double?) {
        _citiesList.value = getCitiesUseCase.invoke(latitude, longitude).list
        Log.e("location", latitude.toString())
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        fun provideFactory(
            getCitiesUseCase: GetCitiesUseCase,
            getLocationUseCase: GetLocationUseCase,
            getWeatherByNameUseCase: GetWeatherByNameUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    getLocationUseCase,
                    getCitiesUseCase,
                    getWeatherByNameUseCase)
            }
        }
    }
}
