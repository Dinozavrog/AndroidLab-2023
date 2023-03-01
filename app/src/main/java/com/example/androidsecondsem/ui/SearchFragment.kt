package com.example.androidsecondsem.ui

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.androidsecondsem.R
import com.example.androidsecondsem.data.response.City
import com.example.androidsecondsem.data.response.Container
import com.example.androidsecondsem.databinding.FragmentSearchBinding
import com.example.androidsecondsem.recycler.CityAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.example.androidsecondsem.utils.hideKeyboard
import kotlinx.coroutines.launch
import timber.log.Timber
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

private const val REQUEST_CODE_100 = 100
private const val DEFAULT_LAT = 51.59
private const val DEFAULT_LON = 45.96

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var binding: FragmentSearchBinding? = null
    private val api = Container.weatherApi
    private var cityId: Int? = null
    private lateinit var userLocation: FusedLocationProviderClient
    private var userLatitude: Double = DEFAULT_LAT
    private var userLongitude: Double = DEFAULT_LON
    private lateinit var cities: List<City>
    private lateinit var cityAdapter: CityAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)

        createLocationList()

        binding?.run {
            btnLoad.setOnClickListener {
                onLoadClick()
            }
            etCity.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onLoadClick()
                }
                true
            }
        }
    }

    private fun createCityRecyclerView() {
        lifecycleScope.launch {
            try {
                cities = Container.getCities(userLatitude, userLongitude, 10)
                cityAdapter = CityAdapter(cities) {
                    navigateToCityFragment(it)
                }
                binding?.rvTaro?.adapter = cityAdapter
            } catch (ex: Exception) {
                Timber.e(ex.message.toString())
            }
        }
    }

    private fun onLoadClick() {
        binding?.run {
            etCity.hideKeyboard()
            loadWeather(etCity.text.toString())
        }
    }

    private fun loadWeather(query: String) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                api.getWeather(query).also {
                    cityId = it.id
                    showTemp(it.id)
                    it.weather.firstOrNull()?.also {
                        showWeatherIcon(it.icon)
                    }
                    navigateToCityFragment(cityId)
                }
            } catch (error: Throwable) {
                showError(error)
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
        binding?.progress?.isVisible = isShow
    }

    private fun showError(error: Throwable) {
        Toast.makeText(this.requireContext(), error.message ?: "Error", Toast.LENGTH_SHORT).show()
    }

    private fun showTemp(temp: Int) {
        binding?.tvTemp?.run {
            text = "Temp: $temp C"
            isVisible = true
        }
    }

    private fun showWeatherIcon(id: String) {
        binding?.ivIcon?.load("https://openweathermap.org/img/w/$id.png") {
            crossfade(true)
        }
    }

    private fun navigateToCityFragment(cityId: Int?) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.container_of_fragments,
                CityFragment.newInstance(cityId = cityId),
            )
            .addToBackStack(null)
            .commit()
    }

    private fun createLocationList() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_DENIED) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            requestPermissions(permissions, REQUEST_CODE_100)
        } else {
            userLocation = LocationServices.getFusedLocationProviderClient(requireActivity())
            userLocation.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    userLongitude = location.longitude
                    userLatitude = location.latitude
                    Snackbar.make(binding!!.root, "Location was found", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(binding!!.root, "Location was not found", Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
        createCityRecyclerView()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_100 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createLocationList()
                } else {
                    Snackbar.make(binding!!.root, "Geolocation access denied", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    companion object {
        const val SEARCH_FRAGMENT_TAG = "SEARCH_FRAGMENT_TAG"
        fun newInstance(bundle: Bundle) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putAll(bundle)
                }
            }
    }
}
