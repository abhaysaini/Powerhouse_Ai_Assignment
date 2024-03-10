package com.example.powerhouse_ai_assignment.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.powerhouse_ai_assignment.data.database.AppDatabase.Companion.VERSION
import com.example.powerhouse_ai_assignment.data.database.dao.WeatherDao
import com.example.powerhouse_ai_assignment.data.database.entity.WeatherData

@Database(entities = [WeatherData::class], version = VERSION)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {

        const val VERSION = 1
        const val NAME = "weather-db"

    }
}