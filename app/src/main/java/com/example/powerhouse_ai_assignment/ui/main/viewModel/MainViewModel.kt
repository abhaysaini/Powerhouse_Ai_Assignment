package com.example.powerhouse_ai_assignment.ui.main.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powerhouse_ai_assignment.data.model.WeatherResponse
import com.example.powerhouse_ai_assignment.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse: LiveData<WeatherResponse>
        get() = _weatherResponse

    private val _weatherResponseForCity = MutableLiveData<WeatherResponse>()
    val weatherResponseForCity: LiveData<WeatherResponse>
        get() = _weatherResponseForCity

    suspend fun getWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getResponse(latitude, longitude)
            _weatherResponse.postValue(response)
            Log.i("abhay", response.toString())
        }
    }

    suspend fun getWeatherForCity(cityName:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getResponseForCity(cityName)
            _weatherResponseForCity.postValue(response)
            Log.i("abhay", response.toString())
        }
    }

    suspend fun getSavedWeatherResponse(): WeatherResponse? {
        return repository.getSavedWeatherResponse()
    }
}