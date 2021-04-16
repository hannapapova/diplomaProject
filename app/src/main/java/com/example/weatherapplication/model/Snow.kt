package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "snow_table")
data class Snow (
    @PrimaryKey
    @field:Json(name = "1h")
    val snowVolume: Float
)
