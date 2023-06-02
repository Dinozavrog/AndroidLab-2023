package com.example.androidsecondsem.data.weather

import com.example.androidsecondsem.data.weather.response.CitiesResponse
import com.example.androidsecondsem.data.weather.response.WeatherApi
import com.example.androidsecondsem.data.weather.response.WeatherResponse
import com.example.androidsecondsem.domain.weather.model.CitiesListInfo
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
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
class WeatherRepositoryImplTest {

    @MockK
    lateinit var weatherApi: WeatherApi

    private lateinit var weatherRepositoryImpl: WeatherRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        weatherRepositoryImpl = WeatherRepositoryImpl(weatherApi)
    }

    private val expectedWeatherResponse = mockk<WeatherResponse> {
        every { name } returns "Kazan"
        every { id } returns 1
        every { weather } returns listOf(
            mockk {
                every { description } returns ""
                every { icon } returns ""
            }
        )
        every { clouds } returns mockk {
            every { all } returns 0
        }
        every { main } returns mockk {
            every { temp } returns 21.1
        }
        every { wind } returns mockk {
            every { deg } returns 0
        }
    }

    private val expectedCitiesResponse = mockk<CitiesResponse> {
        every { cod } returns "200"
        every { count } returns 10
        every { list } returns null
        every { message } returns "test"
    }

    @Test
    fun whenCallGetWeatherByNameExpectedSuccess() = runTest() {
        val query = "Kazan"
        val expectedResult = WeatherInfo(
            name = "Kazan",
            weatherDescription = "",
            icon = "",
            wind = 0,
            temperature = 21.1,
            clouds = "0%",
            id = 1
        )
        coEvery {
            weatherApi.getWeatherByName(query)
        } returns expectedWeatherResponse
        val result = weatherRepositoryImpl.getWeatherByName(query)
        assertEquals(expectedResult, result)
    }

    @Test
    fun whenCallGetWeatherByNameError() = runTest {
        val query = "Kazan"
        coEvery {
            weatherApi.getWeatherByName(query)
        } throws Throwable("test")
        assertFailsWith<Throwable> {
            weatherRepositoryImpl.getWeatherByName(query)
        }
    }

    @Test
    fun whenCallGetWeatherByIdExpectedSuccess() = runTest() {
        val id = "1"
        val expectedResult = WeatherInfo(
            name = "Kazan",
            weatherDescription = "",
            icon = "",
            wind = 0,
            temperature = 21.1,
            clouds = "0%",
            id = 1
        )
        coEvery {
            weatherApi.getWeatherById(id)
        } returns expectedWeatherResponse
        val result = weatherRepositoryImpl.getWeatherById(id)
        assertEquals(expectedResult, result)
    }

    @Test
    fun whenCallGetWeatherByIdError() = runTest {
        val id = "1"
        coEvery {
            weatherApi.getWeatherById(id)
        } throws Throwable("test")
        assertFailsWith<Throwable> {
            weatherRepositoryImpl.getWeatherById(id)
        }
    }

    @Test
    fun whenCallGetNearestCitiesSuccess() = runTest() {
        val lat = 0.0
        val lon = 0.0
        val expectedResult = CitiesListInfo(
            cod = "200",
            count = 10,
            list = null,
            message = "test"
        )
        coEvery {
            weatherApi.getCities(lat, lon)
        } returns expectedCitiesResponse
        val result = weatherRepositoryImpl.getCities(lat, lon)
        assertEquals(expectedResult, result)
    }

    @Test
    fun whenCallGetNearestCitiesError() = runTest {
        val lat = 0.0
        val lon = 0.0
        coEvery {
            weatherApi.getCities(lat, lon)
        } throws  Throwable("test")
        assertFailsWith<Throwable> {
            weatherRepositoryImpl.getCities(lat, lon)
        }
    }
}
