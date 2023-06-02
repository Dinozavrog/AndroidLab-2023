package com.example.androidsecondsem.domain.weather.useCase

import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.repository.WeatherRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetCitiesUseCaseTest {

    @MockK
    lateinit var weatherRepository: WeatherRepository

    private lateinit var getCitiesUseCase: GetCitiesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCitiesUseCase = GetCitiesUseCase(weatherRepository)
    }

    @Test
    fun whenGetCitiesUseCaseSuccess() {
        val lat = 0.0
        val lon = 0.0
        val expectedData: CitiesListInfo = mockk()

        coEvery {
            weatherRepository.getCities(lat, lon)
        } returns expectedData

        runTest {
            val result = getCitiesUseCase.invoke(lat, lon)

            assertEquals(expectedData, result)
        }
    }

    @Test
    fun whenGetCitiesUseCaseError() {
        val lat = 0.0
        val lon = 0.0
        coEvery {
            weatherRepository.getCities(lat, lon)
        } throws RuntimeException()

        runTest {
            assertFailsWith<RuntimeException> {
                getCitiesUseCase.invoke(lon, lat)
            }
        }
    }
}
