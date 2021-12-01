package com.example.weatherappctn.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import com.example.weatherappctn.databinding.ActivitySplashScreenBinding


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.root.alpha = 0f

        binding.root.animate().setDuration(5500).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}