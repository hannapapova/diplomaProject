package com.example.weatherapplication.model

import androidx.room.Entity
import com.squareup.moshi.Json

@Entity(tableName = "rain_table")
data class Rain(
    @field:Json(name = "1h")
    val rainVolume: Float
)
