package com.example.weatherapplication.model

import com.squareup.moshi.Json

data class Snow (
    @field:Json(name = "1h")
    val snowVolume: Float
)
