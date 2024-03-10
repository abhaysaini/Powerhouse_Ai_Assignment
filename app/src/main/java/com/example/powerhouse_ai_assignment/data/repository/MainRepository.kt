package com.example.powerhouse_ai_assignment.data.repository

import com.example.powerhouse_ai_assignment.data.api.RetrofitApiInterface
import com.example.powerhouse_ai_assignment.data.database.AppDatabase
import com.example.powerhouse_ai_assignment.data.database.dao.WeatherDao
import com.example.powerhouse_ai_assignment.data.model.WeatherResponse
import com.example.powerhouse_ai_assignment.utils.Constants.API_KEY
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiInterface: RetrofitApiInterface, private val weatherDao: WeatherDao) {

    suspend fun getResponse(latitude:Double, longitude:Double): WeatherResponse {
        return withContext(Dispatchers.IO) {
            apiInterface.getWeather(latitude,longitude,API_KEY)
        }
    }

    suspend fun getSavedWeatherResponse(): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            val weatherData = weatherDao.getLatestWeatherData()
            weatherData?.let {
                Gson().fromJson(weatherData.responseJson, WeatherResponse::class.java)
            }
        }
    }
}