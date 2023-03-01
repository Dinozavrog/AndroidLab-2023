package com.example.androidsecondsem.utils

class WindConverter(private val digit: Int) {

    fun convertWind(): String {
        return when(digit) {
            360 or 0 -> "N"
            270 -> "W"
            90 -> "E"
            180 -> "S"
            45 -> "EN"
            135 -> "SE"
            225 -> "SW"
            315 -> "NW"
            in 46..89 -> "EEN"
            in 1..44 -> "NNE"
            in 91..134 -> "EES"
            in 136..179 -> "SSE"
            in 181..224 -> "SSW"
            in 226..269 -> "SWW"
            in 271..314 -> "NWW"
            in 316..359 -> "NNW"
            else -> ""
        }
    }
}
