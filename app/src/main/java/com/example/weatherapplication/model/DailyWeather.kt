package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "daily_weather_table")
data class DailyWeather(
    @PrimaryKey
    @field:Json(name = "dt")
    val dateTime: Int,
    val sunrise: Int,
    val sunset: Int,
    @field:Json(name = "temp")
    val temperature: Temperature,
    @field:Json(name = "feels_like")
    val feelsLike: FeelsLike,
    val pressure: Int,
    val humidity: Int,
    @field:Json(name = "dew_point")
    val dewPoint: Float,
    @field:Json(name = "uvi")
    val uvIndex: Float,
    val clouds: Int,
    @field:Json(name = "wind_speed")
    val windSpeed: Float,
    @field:Json(name = "wind_deg")
    val windDirection: Int,
    @field:Json(name = "wind_gust")
    val windGust: Float,
    @field:Json(name = "pop")
    val probabilityOfPrecipitation: Float,
    val rain: Float,
    val snow: Float,
    val weather: List<Weather>
)