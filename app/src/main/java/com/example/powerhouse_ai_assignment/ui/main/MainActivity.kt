package com.example.powerhouse_ai_assignment.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.powerhouse_ai_assignment.R
import com.example.powerhouse_ai_assignment.data.database.AppDatabase
import com.example.powerhouse_ai_assignment.data.database.dao.WeatherDao
import com.example.powerhouse_ai_assignment.data.database.entity.WeatherData
import com.example.powerhouse_ai_assignment.data.model.Weather
import com.example.powerhouse_ai_assignment.data.model.WeatherResponse
import com.example.powerhouse_ai_assignment.databinding.ActivityMainBinding
import com.example.powerhouse_ai_assignment.ui.main.viewModel.MainViewModel
import com.example.powerhouse_ai_assignment.utils.NetworkUtils
import com.example.powerhouse_ai_assignment.utils.TimeUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    @Inject
    lateinit var weatherDao: WeatherDao

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                fetchLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                appSettingOpen(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (hasLocationPermission() && NetworkUtils.isNetworkAvailable(this)) {
            fetchLocation()
        } else if (!(NetworkUtils.isNetworkAvailable(this)) && hasLocationPermission()) {
            savedLocalData()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        observeWeatherData()
    }

    private fun observeWeatherData() {
        viewModel.weatherResponse.observe(this) { weatherResponse ->
            weatherResponse?.let {
                updateUI(it)
                CoroutineScope(Dispatchers.IO).launch {
                    saveWeatherResponseLocally(weatherResponse)
                }
            }
        }
    }

    private fun savedLocalData() {
        CoroutineScope(Dispatchers.Main).launch {
            val savedWeatherResponse = viewModel.getSavedWeatherResponse()
            if (savedWeatherResponse != null) {
                updateUI(savedWeatherResponse)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        CoroutineScope(Dispatchers.Main).launch {
            val savedWeatherResponse = viewModel.getSavedWeatherResponse()
            if (savedWeatherResponse != null) {
                updateUI(savedWeatherResponse)
            }
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val latitude = location.latitude
                val longitude = location.longitude
                Log.i("abhay", "$latitude -----  $longitude")
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getWeather(latitude, longitude)
                }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Failed to fetch location: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun updateUI(weatherResponse: WeatherResponse) {
        val sdf = SimpleDateFormat("EEE, MMM d, yyyy")
        val currentDate = sdf.format(Date(weatherResponse.dt.toLong() * 1000))
        val temperature = weatherResponse.main.temp - 273.15

        binding.apply {
            cityName.text = weatherResponse.name
            countryName.text = TimeUtil.getCountryNameByCode(weatherResponse.sys.country)
            countryName.text = weatherResponse.sys.country
            date.text = currentDate
            setWeatherIcon(weatherResponse.weather[0])
            weatherNumericValue.text = String.format("%.1f", temperature)
            weatherUnit.text = getString(R.string.celsius)
            weatherType.text = weatherResponse.weather[0].description
            atmosphericPressureValue.text = weatherResponse.main.pressure.toString()
            windValue.text = (weatherResponse.wind.speed * 100).toString()
            humidityValue.text = weatherResponse.main.humidity.toString()
            tomorrowSunrise.text =
                TimeUtil.convertUnixTimestampToTime(weatherResponse.sys.sunrise.toLong())
            tomorrowSunset.text =
                TimeUtil.convertUnixTimestampToTime(weatherResponse.sys.sunset.toLong())
        }
    }

    private fun setWeatherIcon(weatherType: Weather) {
        if (weatherType.id in 200..232) { //Thunderstorm
            binding.weatherIcon.setImageResource(R.drawable.icon_weather_thunderstorm_cloud)
        } else if (weatherType.id in 300..321) { //Drizzle
            binding.weatherIcon.setImageResource(R.drawable.icon_weather_rain_cloud)
        } else if (weatherType.id in 500..531) { //Rain
            binding.weatherIcon.setImageResource(R.drawable.icon_weather_sun_rain_cloud)
        } else if (weatherType.id in 701..781) { //Atmosphere
            binding.weatherIcon.setImageResource(R.drawable.icon_weather_cloud_sun)
        } else if (weatherType.id in 600..622) { //Snow
            binding.weatherIcon.setImageResource(R.drawable.icon_weather_snow_cloud)
        } else if (weatherType.id == 800) { //Clear
            binding.weatherIcon.setImageResource(R.drawable.icon_weather_sun)
        } else if (weatherType.id in 801..804) { //Cloud
            binding.weatherIcon.setImageResource(R.drawable.icon_weather_cloud)
        } else {
            binding.weatherIcon.setImageResource(R.drawable.icon_weather_sun)
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun appSettingOpen(activity: Activity) {
        Toast.makeText(
            activity,
            "Go to Setting and Enable All Permission",
            Toast.LENGTH_LONG
        ).show()

        val settingIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        settingIntent.data = Uri.parse("package:${activity.packageName}")
        activity.startActivityForResult(settingIntent, 1001)
    }

    private suspend fun saveWeatherResponseLocally(weatherResponse: WeatherResponse) {
        val weatherData = WeatherData(
            1,
            Gson().toJson(weatherResponse),
            System.currentTimeMillis()
        )
        weatherDao.insert(weatherData)
    }
}