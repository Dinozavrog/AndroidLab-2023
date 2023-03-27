package com.example.androidsecondsem.presentation.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidsecondsem.R
import com.example.androidsecondsem.data.response.WeatherResponse
import com.example.androidsecondsem.databinding.ItemCityBinding

class CityItem(
    private val binding: ItemCityBinding,
    private val action: (WeatherResponse) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var weatherResponse: WeatherResponse? = null

    init {
        itemView.setOnClickListener {
            weatherResponse?.also(action)
        }
    }

    @SuppressLint("SetTextI18n")
    fun onBind(weatherResponse: WeatherResponse) {
        this.weatherResponse = weatherResponse
        with(binding) {
            weatherResponse.weather.firstOrNull()?.also {
                ivCity.load("https://openweathermap.org/img/w/${it.icon}.png") {
                    crossfade(true)
                }
            }
            tvName.text = weatherResponse.name
            val temp = weatherResponse.main.temp
            val color = setTempColor(temp)
            tvTemp.text = temp.toString() + "°C"
            tvTemp.setTextColor(color)
        }
    }

    private fun setTempColor(temp: Double?): Int {
        var color = 0
        if (temp != null) {
            when (temp) {
                in -100.0..-20.01 -> color = R.color.purple_700
                in -20.0..-0.01 -> color = R.color.purple_500
                0.0 -> color = R.color.teal_700
                in 0.01..20.09 -> color = R.color.temp_fifth
                in 20.1..100.0 -> color = R.color.temp_eighth
            }
        }
        return color
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (WeatherResponse) -> Unit,
        ): CityItem = CityItem(
            binding = ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            action = action,
        )
    }
}
