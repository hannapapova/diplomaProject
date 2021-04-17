package com.example.weatherapplication.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather_table")
data class SavedCurrentWeather(
    @PrimaryKey
    val dateTime: Int,
    val sunrise: Int,
    val sunset: Int,
    val temperature: Float,
    val feelsLike: Float,
    val pressure: Int,
    val humidity: Int,
    val dewPoint: Float,
    val clouds: Int,
    val uvIndex: Float,
    val visibility: Int,
    val windSpeed: Float,
    val windDirection: Int,
    val rainVolume: Float,
    val snowVolume: Float,
    val weatherMain: String,
    val weatherDescription: String
)