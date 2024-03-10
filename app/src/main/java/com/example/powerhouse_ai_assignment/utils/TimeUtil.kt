package com.example.powerhouse_ai_assignment.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TimeUtil {

    companion object {
        fun extractTimeFromString(dateTimeString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = inputFormat.parse(dateTimeString)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                ""
            }
        }

        fun getCountryNameByCode(countryCode: String) : String {
            val locale = Locale("", countryCode)
            return locale.displayCountry
        }

        fun convertUnixTimestampToTime(unixTimestamp: Long): String {
            return try {
                val date = Date(unixTimestamp * 1000L)
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                outputFormat.format(date)
            } catch (e: Exception) {
                ""
            }
        }

        fun convertDateStringToDay(dateString: String): String? {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val date = inputFormat.parse(dateString)

                if (date != null) {
                    val outputFormat = SimpleDateFormat("EEEE", Locale.US)
                    return outputFormat.format(date)
                }
            } catch (_: Exception) {}

            return ""
        }

    }

     fun isCurrentLocalTime(timeString: String): Boolean {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = Calendar.getInstance()
        val parsedTime = sdf.parse(timeString)

        if (parsedTime != null) {
            val parsedCalendar = Calendar.getInstance()
            parsedCalendar.time = parsedTime

            return currentTime.get(Calendar.HOUR_OF_DAY) == parsedCalendar.get(Calendar.HOUR_OF_DAY)
        }

        return false
    }

}