package com.example.androidsecondsem.domain.weather.useCase

import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetWeatherByIdUseCaseTest {

    @MockK
    lateinit var weatherRepository: WeatherRepository

    private lateinit var getWeatherByIdUseCase: GetWeatherByIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getWeatherByIdUseCase = GetWeatherByIdUseCase(weatherRepository = weatherRepository)
    }

    @Test
    fun whenGetWeatherByIdUseCaseSuccess() {
        val id = "0"
        val expectedTemp = 21.0
        val expectedData: WeatherInfo = mockk {
            every { temperature } returns expectedTemp
            every { wind } returns 100
        }
        coEvery {
            weatherRepository.getWeatherById(id)
        } returns expectedData

        runTest {
            val result = getWeatherByIdUseCase.invoke(id)

            assertEquals(expectedData, result)
            assertEquals(expectedTemp, result.temperature, 0.001)
        }
    }

    @Test
    fun whenGetWeatherByIdYseCaseError() {
        val id = "0"
        coEvery {
            weatherRepository.getWeatherById(id)
        } throws RuntimeException()

        runTest {
            assertFailsWith<RuntimeException> {
                getWeatherByIdUseCase.invoke(id)
            }
        }
    }
}
