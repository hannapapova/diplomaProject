package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "current_weather_table")
data class CurrentWeather(
    @PrimaryKey
    @field:Json(name = "dt")
    val dateTime: Int,
    val sunrise: Int,
    val sunset: Int,
    @field:Json(name = "temp")
    val temperature: Float,
    @field:Json(name = "feels_like")
    val feelsLike: Float,
    val pressure: Int,
    val humidity: Int,
    @field:Json(name = "dew_point")
    val dewPoint: Float,
    val clouds: Int,
    @field:Json(name = "uvi")
    val uvIndex: Float,
    val visibility: Int,
    @field:Json(name = "wind_speed")
    val windSpeed: Float,
    @field:Json(name = "wind_gust")
    val windGust: Float,
    @field:Json(name = "wind_deg")
    val windDirection: Int,
    val rain: Rain,
    val snow: Snow,
    val weather: List<Weather>
)