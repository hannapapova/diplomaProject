package com.example.weatherapplication.model.forecast

import com.squareup.moshi.Json

data class Snow(
    @field:Json(name = "1h")
    val snowVolume: Float
)
