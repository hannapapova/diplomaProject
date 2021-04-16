package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "temperature_table")
data class Temperature(
    @PrimaryKey
    val day: Float,
    val night: Float,
    @field:Json(name = "morn")
    val morning: Float,
    @field:Json(name = "eve")
    val evening: Float,
    val min: Float,
    val max: Float
)
