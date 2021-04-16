package com.example.weatherapplication.model

import androidx.room.Entity
import com.squareup.moshi.Json

@Entity(tableName = "minutely_weather_table")
data class MinutelyWeather(
    @field:Json(name = "dt")
    val dateTime: Int,
    val precipitation: Float
)