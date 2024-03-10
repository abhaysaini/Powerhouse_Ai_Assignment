package com.example.powerhouse_ai_assignment.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.powerhouse_ai_assignment.data.database.entity.WeatherData

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherData: WeatherData)

    @Query("SELECT * FROM weather_data ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestWeatherData(): WeatherData?
}