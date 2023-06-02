package com.example.androidsecondsem.data.location

import android.location.Location
import com.example.androidsecondsem.domain.location.model.UserLocationModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class LocationDataSourceTest {

    @MockK
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var dataSource: LocationDataSource

    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = LocationDataSource(fusedLocationProviderClient, testDispatcher)
    }

    private val expectedLocationResponse = mockk<Task<Location>> {
        every { result } returns mockk {
            every { latitude } returns 0.0
            every { longitude } returns 0.0
        }
        every { isComplete } returns true
        every { exception } returns null
        every { isCanceled } returns false
    }

    @Test
    fun whenCallGetLocationSuccess() = runTest(testDispatcher) {
        val expectedResult = UserLocationModel(
            latitude = 0.0,
            longitude = 0.0
        )
        coEvery {
            fusedLocationProviderClient.lastLocation
        } returns expectedLocationResponse
        val result = dataSource.getLocation()
        assertEquals(expectedResult, result)
    }

    @Test
    fun whenCallGetLocationError() = runTest {
        coEvery {
            fusedLocationProviderClient.lastLocation
        } throws Throwable("test")
        assertFailsWith<Throwable> {
            dataSource.getLocation()
        }
    }
}
