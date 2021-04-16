package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "response_table")
data class Response(
    @PrimaryKey
    @field:Json(name = "lat")
    val latitude: Float,
    @field:Json(name = "lon")
    val longitude: Float,
    val timezone: String,
    @field:Json(name = "timezone_offset")
    val timezoneOffset: Int,
    val current: CurrentWeather,
    val minutely: List<MinutelyWeather>,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>,
    val alerts: List<Alert>?
)