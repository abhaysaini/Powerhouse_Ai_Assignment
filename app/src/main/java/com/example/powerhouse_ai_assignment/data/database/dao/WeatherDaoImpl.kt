package com.example.powerhouse_ai_assignment.data.database.dao

import com.example.powerhouse_ai_assignment.data.database.entity.WeatherData
import javax.inject.Inject

class WeatherDaoImpl @Inject constructor(private val weatherDao: WeatherDao) {
    suspend fun insert(weatherData: WeatherData) {
        weatherDao.insert(weatherData)
    }

    suspend fun getLatestWeatherData(): WeatherData? {
        return weatherDao.getLatestWeatherData()
    }
}
