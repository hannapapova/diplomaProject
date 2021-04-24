package com.example.weatherapplication.model.forecast

import com.squareup.moshi.Json

data class Rain(
    @field:Json(name = "1h")
    val rainVolume: Float
)
