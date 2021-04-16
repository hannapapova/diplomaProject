package com.example.weatherapplication.model

import androidx.room.Entity

@Entity(tableName = "weather_table")
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
