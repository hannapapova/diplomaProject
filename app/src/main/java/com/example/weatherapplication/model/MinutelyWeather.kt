package com.example.weatherapplication.model

import com.squareup.moshi.Json

data class MinutelyWeather(
    @field:Json(name = "dt")
    val dateTime: Int,
    val precipitation: Float
)