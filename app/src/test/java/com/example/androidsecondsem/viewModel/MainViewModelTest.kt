package com.example.androidsecondsem.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByIdUseCase
import com.example.androidsecondsem.presentation.fragments.viewModel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
class MainViewModelTest {

    @MockK
    lateinit var getWeatherByIdUseCase: GetWeatherByIdUseCase

    private val cityId = "1"

    private lateinit var viewModel: MainViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = MainViewModel(
            getWeatherByIdUseCase,
            cityId
        )
    }

    @Test
    fun onCallLoadWeather() = runTest {
        val expectedData: WeatherInfo = mockk()
        coEvery { getWeatherByIdUseCase.invoke(cityId) } returns expectedData

        viewModel.loadWeather()

        assertEquals(expectedData, viewModel.weatherInfo.value)
    }
}
