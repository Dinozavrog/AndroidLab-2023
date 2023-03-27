package com.example.androidsecondsem.presentation.fragments.viewModel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.androidsecondsem.data.response.WeatherResponse
import com.example.androidsecondsem.data.response.Container
import com.example.androidsecondsem.domain.location.model.UserLocationModel
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.useCase.GetCitiesUseCase
import com.example.androidsecondsem.domain.location.useCase.GetLocationUseCase
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByIdUseCase
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByNameUseCase
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getWeatherByIdUseCase: GetWeatherByIdUseCase,
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
        val cityId: Int? = weatherResponse.id
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
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val getWeatherByIdUseCase = Container.weatherByIdUseCase
                val getCitiesUseCase = Container.getCities
                val getLocationUseCase = Container.locationUseCase
                val getWeatherByNameUseCase = Container.weatherByNameUseName
                return SearchViewModel(
                    getWeatherByIdUseCase,
                    getLocationUseCase,
                    getCitiesUseCase,
                    getWeatherByNameUseCase) as T
            }
        }
    }
}
