package com.example.weatherapplication.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_cities_table")
data class FavouriteCity(
    @PrimaryKey
    val name: String,
    val adminName1: String,
    val countryName: String,
    val latitude: String,
    val longitude: String,
    var inFavourites: Boolean = true
)