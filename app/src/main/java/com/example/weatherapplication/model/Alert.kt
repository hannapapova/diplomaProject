package com.example.weatherapplication.model

import com.squareup.moshi.Json

data class Alert(
    @field:Json(name = "sender_name")
    val senderName: String,
    val event: String,
    val start: Int,
    val end: Int,
    val description: String
)