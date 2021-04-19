package com.example.weatherapplication.model.forecast

import com.squareup.moshi.Json

data class FeelsLike(
    val day: Float,
    val night: Float,
    @field:Json(name = "morn")
    val morning: Float,
    @field:Json(name = "eve")
    val evening: Float
)
