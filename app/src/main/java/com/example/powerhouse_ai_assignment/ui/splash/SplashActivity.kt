package com.example.powerhouse_ai_assignment.ui.splash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.powerhouse_ai_assignment.R
import com.example.powerhouse_ai_assignment.databinding.ActivitySplashBinding
import com.example.powerhouse_ai_assignment.ui.main.MainActivity
import com.example.powerhouse_ai_assignment.utils.NetworkUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed ({
            navigateToMainActivity()
        }, SPLASH_TIME)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object{
        const val SPLASH_TIME = 2000L
    }
}