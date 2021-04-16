package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "rain_table")
data class Rain(
    @PrimaryKey
    @field:Json(name = "1h")
    val rainVolume: Float
)
