package com.example.androidsecondsem.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.androidsecondsem.R
import com.example.androidsecondsem.data.response.Container
import com.example.androidsecondsem.databinding.FragmentCityBinding
import com.example.androidsecondsem.utils.WindConverter
import kotlinx.coroutines.launch

class CityFragment : Fragment(R.layout.fragment_city) {

    private var binding: FragmentCityBinding? = null
    private var cityId: Int? = null
    private val api = Container.weatherApi
    private var windConverter: WindConverter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCityBinding.bind(view)
        cityId = arguments?.getInt("CITY_ID")

        binding?.run {
            cityId?.let { loadWeather(it) }
        }
    }

    private fun loadWeather(query: Int) {
        lifecycleScope.launch {
            try {
                api.getWeatherById(query).also {
                    binding?.run {
                        tvTemp.text = it.main.temp.toString()
                        tvCity.text = it.name
                        tvWeather.text = it.weather[0].description
                        tvCloudDesc.text = it.clouds.all.toString() + "%"
                        windConverter = WindConverter(it.wind.deg)
                        tvWindDesc.text = windConverter?.convertWind()
                    }
                }
            }
            catch (error: Throwable) {
                showError(error)
            }
        }
    }

    private fun showError(error: Throwable) {
        Toast.makeText(this.requireContext(), error.message ?: "Error", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val CITY_ID = "CITY_ID"
        fun newInstance(cityId: Int?) =
            CityFragment().apply {
                arguments = Bundle().apply {
                    if (cityId != null) {
                        putInt(CITY_ID, cityId)
                    }
                }
            }
    }
}
