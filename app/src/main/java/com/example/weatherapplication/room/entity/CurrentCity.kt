package com.example.weatherapplication.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_city_table")
data class CurrentCity(
    @PrimaryKey
    val name: String,
    val adminName1: String,
    val countryName: String,
    val latitude: String,
    val longitude: String
)