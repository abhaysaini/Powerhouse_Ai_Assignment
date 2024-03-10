package com.example.powerhouse_ai_assignment.ui.bottomsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.powerhouse_ai_assignment.R
import com.example.powerhouse_ai_assignment.data.model.Weather
import com.example.powerhouse_ai_assignment.data.model.WeatherResponse
import com.example.powerhouse_ai_assignment.databinding.FragmentBottomSheetDialogBinding
import com.example.powerhouse_ai_assignment.ui.main.viewModel.MainViewModel
import com.example.powerhouse_ai_assignment.utils.NetworkUtils
import com.example.powerhouse_ai_assignment.utils.TimeUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class BottomSheetDialog (private val cityName : String): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetDialogBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetDialogBinding.inflate(layoutInflater, container, false)
        dialog?.setCanceledOnTouchOutside(false)
        binding.bottomCardView.visibility = View.VISIBLE
        binding.apply {
            cancelButton.setOnClickListener { dismiss() }
        }
        if(NetworkUtils.isNetworkAvailable(requireContext())){
            binding.noNetwork.visibility = View.GONE
            binding.constraintLayout.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getWeatherForCity(cityName)
            }
            observeWeatherData()
        }
        else{
            binding.noNetwork.visibility = View.VISIBLE
            binding.constraintLayout.visibility = View.GONE
        }
        return binding.root
    }

    private fun observeWeatherData() {
        viewModel.weatherResponseForCity.observe(this) { weatherResponse ->
            weatherResponse?.let {
                updateUI(it)
            }
        }
    }

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
}