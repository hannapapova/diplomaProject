package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class Weather(
    @PrimaryKey
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
