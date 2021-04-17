package com.example.weatherapplication.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hourly_weather_table")
data class SavedHourlyWeather(
    @PrimaryKey
    val dateTime: Int,
    val temperature: Float
)