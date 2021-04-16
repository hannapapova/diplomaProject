package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "minutely_weather_table")
data class MinutelyWeather(
    @PrimaryKey
    @field:Json(name = "dt")
    val dateTime: Int,
    val precipitation: Float
)