package com.example.powerhouse_ai_assignment.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.powerhouse_ai_assignment.data.api.RetrofitApiInterface
import com.example.powerhouse_ai_assignment.data.repository.MainRepository
import com.example.powerhouse_ai_assignment.databinding.ActivityMainBinding
import com.example.powerhouse_ai_assignment.ui.main.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latitude = 30.316496
        val longitude = 78.032188

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getWeather(latitude, longitude)
        }
    }
}