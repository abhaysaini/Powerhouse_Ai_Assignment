package com.example.powerhouse_ai_assignment.data.api

import com.example.powerhouse_ai_assignment.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface RetrofitApiInterface {

    @GET("weather")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
}