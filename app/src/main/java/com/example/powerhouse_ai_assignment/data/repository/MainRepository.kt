package com.example.powerhouse_ai_assignment.data.repository

import com.example.powerhouse_ai_assignment.data.api.RetrofitApiInterface
import com.example.powerhouse_ai_assignment.data.model.WeatherResponse
import com.example.powerhouse_ai_assignment.utils.Constants.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiInterface: RetrofitApiInterface) {

    suspend fun getResponse(latitude:Double, longitude:Double): WeatherResponse {
        return withContext(Dispatchers.IO) {
            apiInterface.getWeather(latitude,longitude,API_KEY)
        }
    }
}