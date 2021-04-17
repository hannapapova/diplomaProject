package com.example.weatherapplication.model

import com.squareup.moshi.Json

data class Rain(
    @field:Json(name = "1h")
    val rainVolume: Float
)
