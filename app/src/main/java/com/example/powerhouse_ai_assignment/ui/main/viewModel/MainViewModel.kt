package com.example.powerhouse_ai_assignment.ui.main.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powerhouse_ai_assignment.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) :ViewModel() {

    suspend fun getWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getResponse(latitude, longitude)
            Log.i("abhay",response.toString())
        }
    }
}