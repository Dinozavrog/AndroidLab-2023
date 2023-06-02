package com.example.androidsecondsem.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.androidsecondsem.data.weather.response.WeatherResponse
import com.example.androidsecondsem.domain.location.model.UserLocationModel
import com.example.androidsecondsem.domain.location.useCase.GetLocationUseCase
import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.useCase.GetCitiesUseCase
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByNameUseCase
import com.example.androidsecondsem.presentation.fragments.viewModel.SearchViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @MockK
    lateinit var getWeatherUseCase: GetWeatherByNameUseCase

    @MockK
    lateinit var getLocationUseCase: GetLocationUseCase

    @MockK
    lateinit var getCitiesWeatherUseCase: GetCitiesUseCase

    private lateinit var viewModel: SearchViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = SearchViewModel(
            getLocationUseCase,
            getCitiesWeatherUseCase,
            getWeatherUseCase,
        )
    }

    @Test
    fun whenCallOnWeatherClick() {
        val weatherResponse: WeatherResponse = mockk()
        val cityId = 1
        every { weatherResponse.id } returns cityId

        viewModel.onWeatherClick(weatherResponse)

        assertEquals(cityId, viewModel.navigation.value)
    }

    @Test
    fun whenCallLoadWeather() {
        val cityName = "test"
        val expectedData: WeatherInfo = mockk()
        every { expectedData.name } returns cityName
        coEvery { getWeatherUseCase.invoke(cityName) } returns expectedData

        viewModel.loadWeather(cityName)

        assertEquals(cityName, viewModel.name.value)
    }

    @Test
    fun whenCallLocationPermissionsWithTrueAnswerFromUser() = runTest {
        val res = true
        val expectedData: UserLocationModel = mockk {
            every { latitude } returns 0.0
            every { longitude } returns 0.0
        }
        val error = null
        coEvery { getLocationUseCase.invoke() } returns expectedData
        viewModel.locationPerm(res)

        assertEquals(error, viewModel.error.value)
        assertEquals(expectedData, viewModel.location.value)
    }

    @Test
    fun whenCallLocationPermissionsFalse() = runTest {
        val res = false
        val expectedData = UserLocationModel(
            latitude = 0.0,
            longitude = 0.0
        )
        val error = "error"

        viewModel.locationPerm(res)

        assertEquals(error, viewModel.error.value)
        assertEquals(expectedData, viewModel.location.value)
    }

    @Test
    fun whenCallGetNearestCities() = runTest {

        val latitude = 10.0
        val longitude = 25.0
        val expectedData = CitiesListInfo(
            cod = "200",
            count = 10,
            list = null,
            message = null
        )
        coEvery { getCitiesWeatherUseCase.invoke(latitude, longitude) } returns expectedData
        viewModel.getCities(latitude, longitude)

        assertEquals(null, viewModel.citiesList.value)
    }
}
