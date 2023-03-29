package com.example.androidsecondsem.presentation.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.androidsecondsem.presentation.fragments.viewModel.MainViewModel
import com.example.androidsecondsem.R
import com.example.androidsecondsem.databinding.FragmentCityBinding
import com.example.androidsecondsem.domain.weather.model.WeatherInfo
import com.example.androidsecondsem.domain.weather.useCase.GetWeatherByIdUseCase
import com.example.androidsecondsem.presentation.utils.App
import com.example.androidsecondsem.presentation.utils.WindConverter
import kotlinx.coroutines.launch
import javax.inject.Inject

class CityFragment : Fragment(R.layout.fragment_city) {

    private var binding: FragmentCityBinding? = null
    private var windConverter: WindConverter? = null

    @Inject
    lateinit var getWeatherByIdUseCase: GetWeatherByIdUseCase
    private val viewModel: MainViewModel by viewModels {
        MainViewModel.provideFactory(getWeatherByIdUseCase)
    }

    override fun onAttach(context: Context) {
        App.appComponent.injectCityFragment(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCityBinding.bind(view)
        val cityId = arguments?.getInt("CITY_ID")
        if (cityId != null) {
            getCityWeather(cityId.toString())
        }
        observeViewModel()
    }


    private fun showError(error: Throwable) {
        Toast.makeText(this.requireContext(), error.message ?: "Error", Toast.LENGTH_SHORT).show()
    }

    private fun getCityWeather(id: String) {
        lifecycleScope.launch {
            viewModel.loadWeather(id)
        }
    }

    private fun observeViewModel() {
        viewModel.weatherInfo.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            setCityWeather(it)
        }
    }

    private fun setCityWeather(weatherInfo: WeatherInfo) {
        binding?.run {
            tvTemp.text = weatherInfo.temperature.toString()
            tvCity.text = weatherInfo.name
            tvWeather.text = weatherInfo.weatherDescription
            tvCloudDesc.text = weatherInfo.clouds
            windConverter = WindConverter(weatherInfo.wind)
            tvWindDesc.text = windConverter?.convertWind()
        }
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
