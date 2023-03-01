package com.example.androidsecondsem.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidsecondsem.R
import com.example.androidsecondsem.data.response.City
import com.example.androidsecondsem.databinding.ItemCityBinding

private const val TEMP_50 = 50.0
private const val TEMP_30 = 30.0
private const val TEMP_20 = 20.0
private const val TEMP_15 = 15.0
private const val TEMP_10 = 10.0
private const val TEMP_10_MINUS = -10.0
private const val TEMP_20_MINUS = -20.0
private const val TEMP_40_MINUS = -40.0

class CityItem(
    private val binding: ItemCityBinding,
    private val action: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var city: City? = null

    fun bind(item: City) {
        this.city = item
        with(binding) {
            tvName.text = item.name
            val tvTempText = item.main.temp.toString() + "°C"
            tvTemp.text = tvTempText
            item.weather.firstOrNull()?.also {
                showWeatherIcon(it.icon)
            }
            temperatureColors(item.main.temp, binding.tvTemp)
            itemView.setOnClickListener {
                action(item.id)
            }
        }
    }

    private fun temperatureColors(temp: Double, textView: TextView) {
        when (temp) {
            in TEMP_30..TEMP_50 -> textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    R.color.temp_eighth
                )
            )
            in TEMP_20..TEMP_30 -> textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    R.color.temp_seventh
                )
            )
            in TEMP_15..TEMP_20 -> textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    R.color.temp_sixth
                )
            )
            in TEMP_10..TEMP_15 -> textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    R.color.temp_fifth
                )
            )
            in 0.0..TEMP_10 -> textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    R.color.temp_fourth
                )
            )
            in TEMP_10_MINUS..0.0 -> textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    R.color.temp_third
                )
            )
            in TEMP_20_MINUS..TEMP_10_MINUS -> textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    R.color.temp_second
                )
            )
            in TEMP_40_MINUS..TEMP_20_MINUS -> textView.setTextColor(
                ContextCompat.getColor(
                    textView.context,
                    R.color.temp_first
                )
            )
        }
    }

    private fun showWeatherIcon(id: String) {
        binding.ivCity.load("https://openweathermap.org/img/w/$id.png") {
            crossfade(true)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            action: (Int) -> Unit
        ) = CityItem(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), action
        )
    }
}
