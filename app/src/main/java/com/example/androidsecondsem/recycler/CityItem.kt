package com.example.androidsecondsem.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.androidsecondsem.R
import com.example.androidsecondsem.data.response.City
import com.example.androidsecondsem.databinding.ItemCityBinding

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
            setColor(item.main.temp, binding.tvTemp)
            itemView.setOnClickListener {
                action(item.id)
            }
        }
    }

    private fun setColor(temp: Double, textView: TextView) {
        val color = when {
            temp < -20 -> R.color.temp_first
            temp < -10 -> R.color.temp_second
            temp < 0 -> R.color.temp_third
            temp == 0.0 -> R.color.temp_fourth
            temp > 30 -> R.color.temp_eighth
            temp > 20 -> R.color.temp_seventh
            temp > 10 -> R.color.temp_sixth
            else -> R.color.black }
        textView.setTextColor(color)
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
