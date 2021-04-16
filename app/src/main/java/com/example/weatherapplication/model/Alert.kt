package com.example.weatherapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "alerts_table")
data class Alert(
    @PrimaryKey
    @field:Json(name = "sender_name")
    val senderName: String,
    val event: String,
    val start: Int,
    val end: Int,
    val description: String
)