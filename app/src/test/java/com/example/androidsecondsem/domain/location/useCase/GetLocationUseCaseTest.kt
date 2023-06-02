package com.example.androidsecondsem.domain.location.useCase

import com.example.androidsecondsem.data.location.LocationDataSource
import com.example.androidsecondsem.domain.location.model.UserLocationModel
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
class GetLocationUseCaseTest {

    @MockK
    lateinit var locationDataSource: LocationDataSource

    private lateinit var getLocationUseCase: GetLocationUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getLocationUseCase = GetLocationUseCase(locationDataSource = locationDataSource)
    }

    @Test
    fun whenGetLocationUseCaseSuccess() {
        val expectedData: UserLocationModel = mockk()
        coEvery {
            locationDataSource.getLocation()
        } returns expectedData
        runTest {
            val result = getLocationUseCase.invoke()

            assertEquals(expectedData, result)
        }
    }

    @Test
    fun whenGetLocationUseCaseError() {
        coEvery {
            locationDataSource.getLocation()
        } throws RuntimeException()
        runTest {
            assertFailsWith<RuntimeException> {
                getLocationUseCase.invoke()
            }
        }
    }
}
