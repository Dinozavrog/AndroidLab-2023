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
class GetWeatherByNameUseCaseTest {

    @MockK
    lateinit var weatherRepository: WeatherRepository

    private lateinit var getWeatherByNameUseCase: GetWeatherByNameUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getWeatherByNameUseCase = GetWeatherByNameUseCase(weatherRepository = weatherRepository)
    }

    @Test
    fun whenGetWeatherByNameUseCaseSuccess() {
        val query = "Kazan"
        val expectedTemp = 21.0
        val expectedData: WeatherInfo = mockk {
            every { temperature } returns expectedTemp
            every { wind } returns 100
        }
        coEvery {
            weatherRepository.getWeatherByName(query)
        } returns expectedData

        runTest {
            val result = getWeatherByNameUseCase.invoke(query)

            assertEquals(expectedData, result)
            assertEquals(expectedTemp, result.temperature, 0.001)
        }
    }

    @Test
    fun whenGetWeatherByNameYseCaseError() {
        val query = "Kazan"
        coEvery {
            weatherRepository.getWeatherByName(query)
        } throws RuntimeException()

        runTest {
            assertFailsWith<RuntimeException> {
                getWeatherByNameUseCase.invoke(query)
            }
        }
    }
}
