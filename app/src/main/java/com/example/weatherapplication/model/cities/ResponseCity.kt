package com.example.weatherapplication.model.cities

data class ResponseCity(
    val geonames: List<Geoname>,
    val totalResultsCount: Int
)