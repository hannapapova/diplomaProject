package com.example.weatherapplication.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_weather_table")
data class SavedDailyWeather(
    @PrimaryKey
    val dateTime: Int,
    val sunrise: Int,
    val sunset: Int,
    val dayTemp: Float,
    val nightTemp: Float,
    val morningTemp: Float,
    val eveningTemp: Float,
    val dayFeelsLike: Float,
    val nightFeelsLike: Float,
    val morningFeelsLike: Float,
    val eveningFeelsLike: Float,
    val pressure: Int,
    val humidity: Int,
    val dewPoint: Float,
    val uvIndex: Float,
    val clouds: Int,
    val windSpeed: Float,
    val windDirection: Int,
    val probabilityOfPrecipitation: Float,
    val rain: Float,
    val snow: Float,
    val weatherMain: String,
    val weatherDescription: String
)