package com.example.powerhouse_ai_assignment.di

import android.content.Context
import androidx.room.Room
import com.example.powerhouse_ai_assignment.data.database.AppDatabase
import com.example.powerhouse_ai_assignment.data.database.dao.WeatherDao
import com.example.powerhouse_ai_assignment.data.database.dao.WeatherDaoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        val databaseBuilder = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.NAME
        )
        return databaseBuilder.build()
    }

    @Provides
    fun provideWeatherDao(database: AppDatabase): WeatherDao {
        return database.weatherDao()
    }

}
