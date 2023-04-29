package com.example.androidsecondsem.presentation.fragments.viewModel

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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
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

    var disposable: CompositeDisposable = CompositeDisposable()

    fun onWeatherClick(weatherResponse: WeatherResponse) {
        val cityId: Int = weatherResponse.id
        _navigation.postValue(cityId)
    }

    fun loadWeather(name: String) {
        disposable += getWeatherByNameUseCase(name)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = { weatherInfo ->
                _navigation.postValue(weatherInfo.id)
            }, onError = { error ->
                _error.postValue(error)
            })
    }

    fun locationPerm(res: Boolean) {
        if (res) {
            getLocation()
        } else {
            _location.postValue(UserLocationModel(
                latitude = 54.5299,
                longitude = 52.8039,
            ))
        }
    }

    private fun getLocation() {
        disposable += getLocationUseCase.invoke()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { locationInfo ->
                _location.postValue(locationInfo)
            }
    }

    fun getCities(latitude: Double?, longitude: Double?) {
        disposable += getCitiesUseCase.invoke(latitude, longitude)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { citiesInfo ->
                _citiesList.postValue(citiesInfo.list)
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
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
