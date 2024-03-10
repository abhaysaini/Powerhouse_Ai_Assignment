package com.example.powerhouse_ai_assignment.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_data")
data class WeatherData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "response_json") val responseJson: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)