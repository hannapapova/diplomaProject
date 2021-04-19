package com.example.weatherapplication.model.cities

import com.squareup.moshi.Json

data class Geoname(
    val adminName1: String,
    val countryName: String,
    @field:Json(name = "lat")
    val latitude: String,
    @field:Json(name = "lng")
    val longitude: String,
    val name: String,
)