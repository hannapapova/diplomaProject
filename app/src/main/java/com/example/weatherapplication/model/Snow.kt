package com.example.weatherapplication.model

import androidx.room.Entity
import com.squareup.moshi.Json

@Entity(tableName = "snow_table")
data class Snow (
    @field:Json(name = "1h")
    val snowVolume: Float
)
